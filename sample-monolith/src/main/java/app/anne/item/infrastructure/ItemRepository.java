package app.anne.item.infrastructure;

import app.anne.item.domain.model.aggregates.ItemHistory;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.user.infrastructure.UserStatRecord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues.stringValue;

@Repository
public class ItemRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<ItemRecord> itemTable;
    private final DynamoDbTable<ItemHistoryRecord> itemHistoryTable;
    private final DynamoDbTable<UserStatRecord> userStatTable;

    public ItemRepository(DynamoDbEnhancedClient client,
                          @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.itemTable = client.table(tableName, TableSchema.fromBean(ItemRecord.class));
        this.itemHistoryTable = client.table(tableName, TableSchema.fromBean(ItemHistoryRecord.class));
        this.userStatTable = client.table(tableName, TableSchema.fromBean(UserStatRecord.class));
    }

    public Optional<OwnedItem> findOwnedItem(OwnerId ownerId, ItemId itemId) {
        ItemRecord record = findOne(itemId);
        if (record == null || !ownerId.getValue().equals(record.getOwner())) {
            return Optional.empty();
        }
        OwnedItem ownedItem = ItemRecord.convert(record);
        return Optional.of(ownedItem);
    }

    public ItemRecord findOne(ItemId itemId) {
        Key key = getPrimaryKey(itemId);
        return itemTable.getItem(key);
    }

    public List<ItemRecord> findByOwnerIdAndIsbn13(OwnerId ownerId, String isbn13) {
        Key key = Key.builder()
                .partitionValue(ItemRecord.getGsi1PartitionKeyValue(ownerId))
                .build();
        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(key))
            .filterExpression(Expression.builder()
                .expression("isbn13 = :isbn13")
                .expressionValues(
                        Map.of(":isbn13", stringValue(isbn13))
                )
                .build())
            .scanIndexForward(false)
            .build();
        
        PageIterable<ItemRecord> pagedResults = ((PageIterable<ItemRecord>) itemTable.index("GSI1")
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    public List<ItemRecord> findAll() {
        Key key = Key.builder()
            .partitionValue("ITEM")
            .build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<ItemRecord> pagedResults = ((PageIterable<ItemRecord>) itemTable.index("Inverted")
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();

    }

    public List<ItemRecord> findAll(OwnerId ownerId) {
        Key partitionKey = Key.builder()
            .partitionValue(ItemRecord.getGsi1PartitionKeyValue(ownerId))
            .build();
        QueryConditional queryConditional = QueryConditional.keyEqualTo(partitionKey);
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<ItemRecord> pagedResults = ((PageIterable<ItemRecord>) itemTable.index("GSI1")
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    public ItemRecord saveNew(OwnedItem item, ItemHistory itemHistory, UserStatRecord userStatRecord) {
        return putItemAndItemHistory(item, itemHistory, userStatRecord);
    }

    public void saveTransferred(OwnedItem item, ItemHistory itemHistory, UserStatRecord currentOwnerStatRecord, UserStatRecord newOwnerStatRecord) {
        putItemAndItemHistory(item, itemHistory, currentOwnerStatRecord, newOwnerStatRecord);
    }

    public void removeItem(ItemId itemId) {
        Key key = getPrimaryKey(itemId);
        itemTable.deleteItem(key);
    }

    private ItemRecord putItemAndItemHistory(OwnedItem item, ItemHistory itemHistory, UserStatRecord currentOwnerStatRecord, UserStatRecord newOwnerStatRecord) {
        final ItemRecord itemRecord = ItemRecord.convertFrom(item);
        final ItemHistoryRecord itemHistoryRecord = ItemHistoryRecord.createFromEntity(itemHistory);
        currentOwnerStatRecord.setNumItems(currentOwnerStatRecord.getNumItems() - 1);
        newOwnerStatRecord.setNumItems(newOwnerStatRecord.getNumItems() + 1);
        client.transactWriteItems(b -> b
                .addPutItem(itemTable, itemRecord)
                .addPutItem(itemHistoryTable, itemHistoryRecord)
                .addUpdateItem(userStatTable, currentOwnerStatRecord)
                .addUpdateItem(userStatTable, newOwnerStatRecord)
        );
        return itemRecord;
    }

    private ItemRecord putItemAndItemHistory(OwnedItem item, ItemHistory itemHistory, UserStatRecord userStatRecord) {
        final ItemRecord itemRecord = ItemRecord.convertFrom(item);
        final ItemHistoryRecord itemHistoryRecord = ItemHistoryRecord.createFromEntity(itemHistory);
        userStatRecord.setNumItems(userStatRecord.getNumItems() + 1);
        client.transactWriteItems(b -> b
                .addPutItem(itemTable, itemRecord)
                .addPutItem(itemHistoryTable, itemHistoryRecord)
                .addUpdateItem(userStatTable, userStatRecord)
        );
        return itemRecord;
    }

    private Key getPrimaryKey(ItemId itemId) {
        return Key.builder()
            .partitionValue(ItemRecord.getPartitionKeyValue(itemId))
            .sortValue(ItemRecord.getSortKeyValue())
            .build();
    }
}

