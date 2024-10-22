package app.anne.item.infrastructure;

import app.anne.item.domain.model.aggregates.ItemHistory;
import app.anne.item.domain.model.aggregates.ItemId;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.List;

@DynamoDbBean
@NoArgsConstructor
@Setter
public class ItemHistoryRecord {
    public static ItemHistoryRecord createFromEntity(ItemHistory entity) {
        Instant createdAt = Instant.now();
        ItemHistoryRecord record = new ItemHistoryRecord();
        record.setPk(getPartitionKeyValue(entity.getItemId()));
        record.setSk(getSortKeyValue(createdAt));
        record.setItemId(entity.getItemId().getValue());
        record.setOwnerId(entity.getOwnerId().getValue());
        record.setOwnerName(entity.getOwnerName());
        record.setLocations(entity.getLocations());
        record.setCreatedAt(createdAt);
        return record;
    }

    private static String getPartitionKeyValue(ItemId itemId) {
        return String.format("ITEM#%s", itemId.getValue());
    }

    private static String getSortKeyValue(Instant createdAt) {
        return String.format("HIST#%s", createdAt.toString());
    }

    private String pk;
    private String sk;
    private String itemId;
    private String ownerId;
    private String ownerName;
    private List<String> locations;
    private Instant createdAt;

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

    @DynamoDbAttribute("itmId")
    public String getItemId() {
        return itemId;
    }

    @DynamoDbAttribute("ownId")
    public String getOwnerId() {
        return ownerId;
    }

    @DynamoDbAttribute("ownNm")
    public String getOwnerName() {
        return ownerName;
    }

    @DynamoDbAttribute("locs")
    public List<String> getLocations() {
        return locations;
    }

    @DynamoDbAttribute("ct")
    public Instant getCreatedAt() {
        return createdAt;
    }
}
