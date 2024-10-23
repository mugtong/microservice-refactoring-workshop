package app.anne.warehouse.infrastructure;

import app.anne.item.infrastructure.ItemRecord;
import app.anne.warehouse.domain.GiveItem;
import app.anne.warehouse.domain.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<ItemRecord> itemTable;
    private final DynamoDbTable<WarehouseRecord> warehouseTable;

    public WarehouseRepositoryImpl(DynamoDbEnhancedClient client,
                                   @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.itemTable = client.table(tableName, TableSchema.fromBean(ItemRecord.class));
        this.warehouseTable = client.table(tableName, TableSchema.fromBean(WarehouseRecord.class));
    }

    @Override
    public void saveReceived(GiveItem giveItem) {
        final ItemRecord itemRecord = ItemRecord.convertFrom(giveItem.getItem());
        final WarehouseRecord warehouseRecord = WarehouseRecord.convertFromIncoming(giveItem);
        client.transactWriteItems(b -> b
                .addPutItem(itemTable, itemRecord)
                .addPutItem(warehouseTable, warehouseRecord)
        );
    }
}
