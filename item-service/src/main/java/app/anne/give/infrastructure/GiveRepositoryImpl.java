package app.anne.give.infrastructure;

import app.anne.give.domain.model.*;
import app.anne.give.domain.repository.GiveRecordRepository;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRecord;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
@Slf4j
public class GiveRepositoryImpl implements GiveRepository, GiveRecordRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<GiveRecord> giveTable;
    private final DynamoDbTable<ItemRecord> itemTable;

    public GiveRepositoryImpl(DynamoDbEnhancedClient client,
                              @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.giveTable = client.table(tableName, TableSchema.fromBean(GiveRecord.class));
        this.itemTable = client.table(tableName, TableSchema.fromBean(ItemRecord.class));
    }

    @Override
    public void saveAcceptedGiveAndItem(Give give, OwnedItem item) {
        final GiveRecord ownerGiveRecord = GiveRecord.createOwnerGiveRecord(give);
        final GiveRecord requesterGiveRecord = GiveRecord.createRequesterGiveRecord(give);
        final ItemRecord itemRecord = ItemRecord.convertFrom(item);
        client.transactWriteItems(b -> b
                .addPutItem(giveTable, ownerGiveRecord)
                .addPutItem(giveTable, requesterGiveRecord)
                .addPutItem(itemTable, itemRecord)
        );
    }

    @Override
    public GiveId save(Give give) {
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
        final Key ownerGiveRecordPrimaryKey = ownerGiveRecordPrimaryKey(give.getOwner(), give.getId());
        final Key requesterGiveRecordPrimaryKey = requesterGiveRecordPrimaryKey(give.getRequester(), give.getId());
        client.transactWriteItems(b -> b
                .addDeleteItem(giveTable, ownerGiveRecordPrimaryKey)
                .addDeleteItem(giveTable, requesterGiveRecordPrimaryKey)
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
    public List<GiveRecord> findByOwner(OwnerId ownerId) {
        Key key = Key.builder()
                .partitionValue(GiveRecord.getOwnerGiveRecordPartitionKeyValue(ownerId))
                .build();
        return queryFromGsi1(key);
    }

    @Override
    public GiveRecord findOneByOwner(OwnerId ownerId, GiveId giveId) {
        Key key = ownerGiveRecordPrimaryKey(ownerId, giveId);
        return getItem(key);
    }

    @Override
    public List<GiveRecord> findByRequester(RequesterId requesterId) {
        Key key = Key.builder()
                .partitionValue(GiveRecord.getRequesterGiveRecordPartitionKeyValue(requesterId))
                .build();
        return queryFromGsi1(key);
    }

    @Override
    public GiveRecord findOneByRequester(RequesterId requesterId, GiveId giveId) {
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

    private List<GiveRecord> queryFromGsi1(Key key) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<GiveRecord> pagedResults = (PageIterable<GiveRecord>) giveTable
                .index("GSI1")
                .query(queryEnhancedRequest);

        return pagedResults.items().stream().toList();
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
