package app.anne.give.infrastructure;

import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.model.RequesterId;
import app.anne.item.domain.model.aggregates.OwnerId;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAutoGeneratedTimestampAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbBean
@Setter
@NoArgsConstructor
public class GiveRecord {

    public static String getOwnerGiveRecordPartitionKeyValue(OwnerId ownerId) {
        return String.format("GIVE#OWN#%s", ownerId.getValue());
    }

    public static String getRequesterGiveRecordPartitionKeyValue(RequesterId requesterId) {
        return String.format("GIVE#REQ#%s", requesterId.getValue());
    }

    public static String getSortKeyValue(GiveId giveId) {
        return String.format("GIVE#%s", giveId.getStringValue());
    }

    public static GiveRecord createOwnerGiveRecord(Give give) {
        String pk = getOwnerGiveRecordPartitionKeyValue(give.getOwnerId());
        String sk = getSortKeyValue(give.getId());
        String gsi1Pk = getOwnerGiveRecordPartitionKeyValue(give.getOwnerId());
        Instant gsi1Sk = give.getLastUpdated();
        String gsi2Pk = give.getItemInQrString();
        String gsi2Sk = give.getItemInQrString() == null ? null : "I";
        return GiveRecord.convertFrom(pk, sk, gsi1Pk, gsi1Sk, gsi2Pk, gsi2Sk, give);
    }

    public static GiveRecord createRequesterGiveRecord(Give give) {
        String pk = getRequesterGiveRecordPartitionKeyValue(give.getRequester());
        String sk = getSortKeyValue(give.getId());
        String gsi1Pk = getRequesterGiveRecordPartitionKeyValue(give.getRequester());
        Instant gsi1Sk = give.getLastUpdated();
        String gsi2Pk = give.getItemOutQrString();
        String gsi2Sk = give.getItemOutQrString() == null ? null : "O";
        return GiveRecord.convertFrom(pk, sk, gsi1Pk, gsi1Sk, gsi2Pk, gsi2Sk, give);
    }

    private static GiveRecord convertFrom(String pk, String sk, String gsi1Pk, Instant gsi1Sk,
                                          String gsi2Pk, String gsi2Sk, Give give) {
        GiveRecord r = new GiveRecord();
        r.pk = pk;
        r.sk = sk;
        r.gsi1Pk = gsi1Pk;
        r.gsi1Sk = gsi1Sk;
        r.gsi2Pk = gsi2Pk;
        r.gsi2Sk = gsi2Sk;
        r.id = give.getId().getStringValue();
        r.itemId = give.getItemId().getValue();
        r.itemIsbn = give.getItem().getIsbn();
        r.itemIsbn13 = give.getItem().getIsbn13();
        r.itemTitle = give.getItem().getTitle();
        r.itemAuthor = give.getItem().getAuthor();
        r.itemPublisher = give.getItem().getPublisher();
        r.itemImageUrl = give.getItem().getImageUrl();
        r.requesterId = give.getRequester().getValue();
        r.ownerId = give.getOwnerId().getValue();
        r.placeId = give.getPlace().getValue();
        r.status = give.getStatus().name();
        r.updatedAt = give.getLastUpdated();
        r.itemInQr = give.getItemInQrString();
        r.itemOutQr = give.getItemOutQrString();
        return r;
    }

    private String pk;
    private String sk;
    private String gsi1Pk;
    private Instant gsi1Sk;
    private String gsi2Pk;
    private String gsi2Sk;
    private String id;
    private String itemId;
    private String itemIsbn;
    private String itemIsbn13;
    private String itemTitle;
    private String itemAuthor;
    private String itemPublisher;
    private String itemImageUrl;
    private String requesterId;
    private String ownerId;
    private String placeId;
    private String status;
    private Instant updatedAt;
    private String itemInQr;
    private String itemOutQr;

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
    @DynamoDbAutoGeneratedTimestampAttribute
    public Instant getGsi1Sk() {
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

    @DynamoDbAttribute("id")
    public String getId() {
        return id;
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

    @DynamoDbAttribute("itmTtl")
    public String getItemTitle() {
        return itemTitle;
    }

    @DynamoDbAttribute("itmAut")
    public String getItemAuthor() {
        return itemAuthor;
    }

    @DynamoDbAttribute("itmPub")
    public String getItemPublisher() {
        return itemPublisher;
    }

    @DynamoDbAttribute("itmImg")
    public String getItemImageUrl() {
        return itemImageUrl;
    }

    @DynamoDbAttribute("rqt")
    public String getRequesterId() {
        return requesterId;
    }

    @DynamoDbAttribute("owner")
    public String getOwnerId() {
        return ownerId;
    }

    @DynamoDbAttribute("plc")
    public String getPlaceId() {
        return placeId;
    }

    @DynamoDbAttribute("stat")
    public String getStatus() {
        return status;
    }

    @DynamoDbAttribute("ut")
    @DynamoDbAutoGeneratedTimestampAttribute
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @DynamoDbAttribute("inQr")
    public String getItemInQr() {
        return itemInQr;
    }

    @DynamoDbAttribute("outQr")
    public String getItemOutQr() {
        return itemOutQr;
    }
}
