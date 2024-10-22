package app.anne.feed.infrastructure;

import java.time.Instant;

import app.anne.feed.domain.model.FeedType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@NoArgsConstructor
@Getter
@Setter
public class FeedRecord {

    public static String getPartitionKeyValue(String feedId) {
        return String.format("ITEM#%s", feedId);
    }

    public static String getSortKeyValue() {
        return "ITEM";
    }

    public static String getGsi2PartitionKeyValue() {
        return "FEED";
    }

    private String pk;
    private String sk;
    private String gsi2Pk;
    private String gsi2Sk;
    private String ownerId;
    private String ownerName;
    private String ownerImageUrl;
    private String itemId;
    private String itemImageUrl;
    private String title;
    private String author;
    private FeedType type;
    private Instant updatedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() {
        return pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return sk;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"GSI2"})
    @DynamoDbAttribute("GSI2_PK")
    public String getGsi2Pk() {
        return gsi2Pk;
    }

    @DynamoDbSecondarySortKey(indexNames = {"GSI2"})
    @DynamoDbAttribute("GSI2_SK")
    public String getGsi2Sk() {
        return gsi2Sk;
    }

    @DynamoDbAttribute("ownerId")
    public String getOwnerId() {
        return ownerId;
    }

    @DynamoDbAttribute("ownerName")
    public String getOwnerName() {
        return ownerName;
    }

    @DynamoDbAttribute("ownerImageUrl")
    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }

    @DynamoDbAttribute("itemId")
    public String getItemId() {
        return itemId;
    }

    @DynamoDbAttribute("imageUrl")
    public String getItemImageUrl() {
        return itemImageUrl;
    }

    @DynamoDbAttribute("title")
    public String getTitle() {
        return title;
    }

    @DynamoDbAttribute("author")
    public String getAuthor() {
        return author;
    }

    @DynamoDbAttribute("type")
    public FeedType getType() {
        return type;
    }

    @DynamoDbAttribute("ut")
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
