package app.anne.item.infrastructure;

import app.anne.give.domain.model.GiveId;
import app.anne.item.domain.model.aggregates.ItemId;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@NoArgsConstructor
@Setter
public class ItemLockRecord {
    public static ItemLockRecord create(ItemId itemId, GiveId giveId) {
        ItemLockRecord record = new ItemLockRecord();
        record.setPk(getPartitionKeyValue(itemId));
        record.setSk(getSortKeyValue());
        record.setGiveId(giveId.getStringValue());
        return record;
    }

    public static String getPartitionKeyValue(ItemId itemId) {
        return String.format("ITEM#%s", itemId.getValue());
    }

    public static String getSortKeyValue() {
        return "LOCK";
    }

    private String pk;
    private String sk;
    private String giveId;

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

    @DynamoDbAttribute("giveId")
    public String getGiveId() {
        return giveId;
    }
}
