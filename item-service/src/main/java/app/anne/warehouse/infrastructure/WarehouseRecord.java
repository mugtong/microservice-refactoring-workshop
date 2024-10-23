package app.anne.warehouse.infrastructure;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.place.domain.model.PlaceId;
import app.anne.warehouse.domain.GiveItem;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;

@DynamoDbBean
@Setter
@NoArgsConstructor
public class WarehouseRecord {

    enum Type {
        IN, OUT;
    }

    public static WarehouseRecord convertFromIncoming(GiveItem giveItem) {
        Instant createdAt = Instant.now();

        WarehouseRecord record = new WarehouseRecord();
        record.setPk(WarehouseRecord.createPartitionKeyValue(giveItem.getPlaceId(), giveItem.getItemId()));
        record.setSk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setGsi1Pk(WarehouseRecord.createGSI1PartitionKeyValue(giveItem.getPlaceId()));
        record.setGsi1Sk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setGsi2Pk(WarehouseRecord.createGSI2PartitionKeyValue(giveItem.getPlaceId(), giveItem.getItemIsbn()));
        record.setGsi2Sk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setType(Type.IN.name());
        record.setPlaceId(giveItem.getPlaceId().getValue());
        record.setUserId(giveItem.getOwnerId().getValue());
        record.setItemId(giveItem.getItemId().getValue());
        record.setItemIsbn(giveItem.getItemIsbn());
        record.setItemTitle(giveItem.getItemTitle());
        record.setItemPublisher(giveItem.getItemPublisher());
        record.setCreatedAt(createdAt);
        return record;
    }

    private static String createPartitionKeyValue(PlaceId placeId, ItemId itemId) {
        return String.format("WRHS#%s#%s", placeId.getValue(), itemId.getValue());
    }

    private static String createSortKeyValue(Instant createdAt) {
        return createdAt.toString();
    }

    private static String createGSI1PartitionKeyValue(PlaceId placeId) {
        return String.format("WRHS#%s", placeId.getValue());
    }

    private static String createGSI2PartitionKeyValue(PlaceId placeId, String itemIsbn) {
        return String.format("WRHS#%s#%s", placeId.getValue(), itemIsbn);
    }

    private String pk;
    private String sk;
    private String gsi1Pk;
    private String gsi1Sk;
    private String gsi2Pk;
    private String gsi2Sk;
    private String type;
    private String placeId;
    private String userId;
    private String userName;
    private String itemId;
    private String itemIsbn;
    private String itemTitle;
    private String itemPublisher;
    private Instant createdAt;

    @DynamoDbAttribute("PK")
    @DynamoDbPartitionKey
    public String getPk() {
        return pk;
    }

    @DynamoDbAttribute("SK")
    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }

    @DynamoDbAttribute("GSI1_PK")
    @DynamoDbSecondaryPartitionKey(indexNames = {"GSI1"})
    public String getGsi1Pk() {
        return gsi1Pk;
    }

    @DynamoDbAttribute("GSI1_SK")
    @DynamoDbSecondarySortKey(indexNames = {"GSI1"})
    public String getGsi1Sk() {
        return gsi1Sk;
    }

    @DynamoDbAttribute("GSI2_PK")
    @DynamoDbSecondaryPartitionKey(indexNames = {"GSI2"})
    public String getGsi2Pk() {
        return gsi2Pk;
    }

    @DynamoDbAttribute("GSI2_SK")
    @DynamoDbSecondarySortKey(indexNames = {"GSI2"})
    public String getGsi2Sk() {
        return gsi2Sk;
    }

    @DynamoDbAttribute("type")
    public String getType() {
        return type;
    }

    @DynamoDbAttribute("plc_id")
    public String getPlaceId() {
        return placeId;
    }

    @DynamoDbAttribute("usr_id")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("usr_nm")
    public String getUserName() {
        return userName;
    }

    @DynamoDbAttribute("itm_id")
    public String getItemId() {
        return itemId;
    }

    @DynamoDbAttribute("itm_isbn")
    public String getItemIsbn() {
        return itemIsbn;
    }

    @DynamoDbAttribute("itm_ttle")
    public String getItemTitle() {
        return itemTitle;
    }

    @DynamoDbAttribute("itm_pub")
    public String getItemPublisher() {
        return itemPublisher;
    }

    @DynamoDbAttribute("ct")
    public Instant getCreatedAt() {
        return createdAt;
    }
}
