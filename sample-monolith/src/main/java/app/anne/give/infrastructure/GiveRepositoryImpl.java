package app.anne.give.infrastructure;

import app.anne.give.application.exception.ItemAlreadyReservedException;
import app.anne.give.domain.model.*;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemLockRecord;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

@Repository
@Slf4j
public class GiveRepositoryImpl implements GiveRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<GiveRecord> giveTable;
    private final DynamoDbTable<WarehouseRecord> warehouseTable;
    private final DynamoDbTable<ItemLockRecord> itemLockTable;

    public GiveRepositoryImpl(DynamoDbEnhancedClient client,
                              @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.giveTable = client.table(tableName, TableSchema.fromBean(GiveRecord.class));
        this.warehouseTable = client.table(tableName, TableSchema.fromBean(WarehouseRecord.class));
        this.itemLockTable = client.table(tableName, TableSchema.fromBean(ItemLockRecord.class));
    }

    @Override
    public GiveId saveNew(Give give) {
        final GiveRecord ownerGiveRecord = GiveRecord.createOwnerGiveRecord(give);
        final GiveRecord requesterGiveRecord = GiveRecord.createRequesterGiveRecord(give);
        client.transactWriteItems(b -> b
                .addPutItem(giveTable, ownerGiveRecord)
                .addPutItem(giveTable, requesterGiveRecord)
        );
        return give.getId();
    }

    @Override
    public void delete(Give give) {
        final Key ownerGiveRecordPrimaryKey = ownerGiveRecordPrimaryKey(give.getOwnerId(), give.getId());
        final Key requesterGiveRecordPrimaryKey = requesterGiveRecordPrimaryKey(give.getRequester(), give.getId());
        client.transactWriteItems(b -> b
                .addDeleteItem(giveTable, ownerGiveRecordPrimaryKey)
                .addDeleteItem(giveTable, requesterGiveRecordPrimaryKey)
        );
    }

    @Override
    public void saveAccepted(Give give) {
        final GiveRecord ownerGiveRecord = GiveRecord.createOwnerGiveRecord(give);
        final GiveRecord requesterGiveRecord = GiveRecord.createRequesterGiveRecord(give);
        final ItemLockRecord itemLockRecord = ItemLockRecord.create(give.getItemId(), give.getId());
        final Expression itemLockNotExists = Expression.builder()
                .expression("attribute_not_exists(PK) AND attribute_not_exists(SK)")
                .build();

        try {
            client.transactWriteItems(b -> b
                    .addPutItem(giveTable, ownerGiveRecord)
                    .addPutItem(giveTable, requesterGiveRecord)
                    .addPutItem(itemLockTable, TransactPutItemEnhancedRequest.builder(ItemLockRecord.class)
                            .item(itemLockRecord)
                            .conditionExpression(itemLockNotExists)
                            .build())
            );
        } catch (TransactionCanceledException e) {
            if (e.cancellationReasons().stream()
                    .anyMatch(reason -> reason != null && "ConditionalCheckFailed".equals(reason.code()))) {
                throw new ItemAlreadyReservedException(give.getItemId(), give.getId());
            }
            throw e;
        }
    }

    @Override
    public void saveWarehoused(Give give) {
        final GiveRecord ownerGiveRecord = GiveRecord.createOwnerGiveRecord(give);
        final GiveRecord requesterGiveRecord = GiveRecord.createRequesterGiveRecord(give);
        final WarehouseRecord warehouseRecord = WarehouseRecord.convertFromIncoming(give);
        client.transactWriteItems(b -> b
                .addPutItem(giveTable, ownerGiveRecord)
                .addPutItem(giveTable, requesterGiveRecord)
                .addPutItem(warehouseTable, warehouseRecord)
        );
    }

    @Override
    public void saveCompleted(Give give) {
        final GiveRecord ownerGiveRecord = GiveRecord.createOwnerGiveRecord(give);
        final GiveRecord requesterGiveRecord = GiveRecord.createRequesterGiveRecord(give);
        final WarehouseRecord warehouseRecord = WarehouseRecord.convertFromOutgoing(give);
        final Key itemLockRecordKey = Key.builder()
                .partitionValue(ItemLockRecord.getPartitionKeyValue(give.getItemId()))
                .sortValue(ItemLockRecord.getSortKeyValue())
                .build();
        client.transactWriteItems(b -> b
                .addPutItem(giveTable, ownerGiveRecord)
                .addPutItem(giveTable, requesterGiveRecord)
                .addPutItem(warehouseTable, warehouseRecord)
                .addDeleteItem(itemLockTable, itemLockRecordKey)
        );
    }

    @Override
    public Give findGiveByOwner(OwnerId ownerId, GiveId giveId) {
        GiveRecord record = findOneByOwner(ownerId, giveId);
        return GiveMapper.convertToEntity(record);
    }

    @Override
    public Give findGiveByRequester(RequesterId requesterId, GiveId giveId) {
        GiveRecord record = findOneByRequester(requesterId, giveId);
        return GiveMapper.convertToEntity(record);
    }

    @Override
    public Optional<Give> findGiveByItemInQr(ItemInQr itemInQr) {
        return findOneByItemInOutQr(itemInQr.getCode())
                .map(GiveMapper::convertToEntity);
    }

    @Override
    public Optional<Give> findGiveByItemOutQr(ItemOutQr itemOutQr) {
        return findOneByItemInOutQr(itemOutQr.getCode())
                .map(GiveMapper::convertToEntity);
    }

    @Override
    public List<Give> findByOwner(OwnerId ownerId) {
        Key key = Key.builder()
                .partitionValue(GiveRecord.getOwnerGiveRecordPartitionKeyValue(ownerId))
                .build();
        return queryFromGsi1(key);
    }

    private GiveRecord findOneByOwner(OwnerId ownerId, GiveId giveId) {
        Key key = ownerGiveRecordPrimaryKey(ownerId, giveId);
        return getItem(key);
    }

    @Override
    public List<Give> findByRequester(RequesterId requesterId) {
        Key key = Key.builder()
                .partitionValue(GiveRecord.getRequesterGiveRecordPartitionKeyValue(requesterId))
                .build();
        return queryFromGsi1(key);
    }

    private GiveRecord findOneByRequester(RequesterId requesterId, GiveId giveId) {
        Key key = requesterGiveRecordPrimaryKey(requesterId, giveId);
        return getItem(key);
    }

    private Key ownerGiveRecordPrimaryKey(OwnerId ownerId, GiveId giveId) {
        return Key.builder()
                .partitionValue(GiveRecord.getOwnerGiveRecordPartitionKeyValue(ownerId))
                .sortValue(GiveRecord.getSortKeyValue(giveId))
                .build();
    }

    private Key requesterGiveRecordPrimaryKey(RequesterId requesterId, GiveId giveId) {
        return Key.builder()
                .partitionValue(GiveRecord.getRequesterGiveRecordPartitionKeyValue(requesterId))
                .sortValue(GiveRecord.getSortKeyValue(giveId))
                .build();
    }

    private List<Give> queryFromGsi1(Key key) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<GiveRecord> pagedResults = (PageIterable<GiveRecord>) giveTable
                .index("GSI1")
                .query(queryEnhancedRequest);

        return pagedResults.items().stream()
                .map(GiveMapper::convertToEntity)
                .toList();
    }

    private GiveRecord getItem(Key key) {
        return giveTable.getItem(key);
    }

    private Optional<GiveRecord> findOneByItemInOutQr(String qrCode) {
        Key key = Key.builder()
                .partitionValue(qrCode)
                .build();
        DynamoDbIndex<GiveRecord> index = giveTable.index("GSI2");
        SdkIterable<Page<GiveRecord>> pagedResult = index.query(QueryConditional.keyEqualTo(key));
        List<GiveRecord> flatResult = pagedResult.stream()
                .flatMap(page -> page.items().stream())
                .toList();

        if (flatResult.size() > 1) {
            log.error("more than 1 rows exists with GSI2_PK: {}", qrCode);
        }

        return flatResult.isEmpty() ? Optional.empty() : Optional.of(flatResult.get(0));
    }
}
