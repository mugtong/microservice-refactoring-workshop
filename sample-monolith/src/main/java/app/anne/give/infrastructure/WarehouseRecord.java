package app.anne.give.infrastructure;

import app.anne.give.domain.model.Give;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.place.domain.model.PlaceId;
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
    public static WarehouseRecord convertFromIncoming(Give give) {
        return convertFrom(give, Type.IN);
    }

    public static WarehouseRecord convertFromOutgoing(Give give) {
        return convertFrom(give, Type.OUT);
    }

    private static WarehouseRecord convertFrom(Give give, Type type) {
        Instant createdAt = Instant.now();

        WarehouseRecord record = new WarehouseRecord();
        record.setPk(WarehouseRecord.createPartitionKeyValue(give.getPlace(), give.getItemId()));
        record.setSk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setGsi1Pk(WarehouseRecord.createGSI1PartitionKeyValue(give.getPlace()));
        record.setGsi1Sk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setGsi2Pk(WarehouseRecord.createGSI2PartitionKeyValue(give.getPlace(), give.getItem().getIsbn13()));
        record.setGsi2Sk(WarehouseRecord.createSortKeyValue(createdAt));
        record.setType(type.name());
        record.setPlaceId(give.getPlace().getValue());
        record.setUserId(type == Type.IN ? give.getOwnerId().getValue() : give.getRequester().getValue());
        record.setItemId(give.getItemId().getValue());
        record.setItemIsbn(give.getItem().getIsbn());
        record.setItemIsbn13(give.getItem().getIsbn13());
        record.setItemTitle(give.getItem().getTitle());
        record.setItemPublisher(give.getItem().getPublisher());
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
    private String itemIsbn13;
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

    @DynamoDbAttribute("plcId")
    public String getPlaceId() {
        return placeId;
    }

    @DynamoDbAttribute("usrId")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("usrNm")
    public String getUserName() {
        return userName;
    }

    @DynamoDbAttribute("itmId")
    public String getItemId() {
        return itemId;
    }

    @DynamoDbAttribute("itmIsbn")
    public String getItemIsbn() {
        return itemIsbn;
    }

    @DynamoDbAttribute("itmIsbn13")
    public String getItemIsbn13() {
        return itemIsbn13;
    }

    @DynamoDbAttribute("itmTtle")
    public String getItemTitle() {
        return itemTitle;
    }

    @DynamoDbAttribute("itmPub")
    public String getItemPublisher() {
        return itemPublisher;
    }

    @DynamoDbAttribute("ct")
    public Instant getCreatedAt() {
        return createdAt;
    }
}
