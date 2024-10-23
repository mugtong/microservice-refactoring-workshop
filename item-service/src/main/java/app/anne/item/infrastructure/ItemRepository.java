package app.anne.item.infrastructure;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepository {

    private final DynamoDbTable<ItemRecord> itemDynamoDbTable;

    public ItemRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                          @Value("${aws.dynamodb.table}") String tableName) {
        this.itemDynamoDbTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(ItemRecord.class));
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
        return itemDynamoDbTable.getItem(key);
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

        PageIterable<ItemRecord> pagedResults = ((PageIterable<ItemRecord>) itemDynamoDbTable.index("GSI1")
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    public ItemRecord save(OwnedItem itemEntity) {
        ItemRecord record = ItemRecord.convertFrom(itemEntity);
        itemDynamoDbTable.putItem(record);
        return record;
    }

    public void removeItem(ItemId itemId) {
        Key key = getPrimaryKey(itemId);
        itemDynamoDbTable.deleteItem(key);
    }

    public ItemRecord updateItem(ItemRecord itemRecord) {
        return itemDynamoDbTable.updateItem(itemRecord);
    }

    private Key getPrimaryKey(ItemId itemId) {
        return Key.builder()
                .partitionValue(ItemRecord.getPartitionKeyValue(itemId))
                .sortValue(ItemRecord.getSortKeyValue())
                .build();
    }
}

