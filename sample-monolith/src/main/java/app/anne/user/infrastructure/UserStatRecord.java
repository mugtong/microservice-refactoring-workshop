package app.anne.user.infrastructure;

import app.anne.user.domain.model.UserId;
import app.anne.user.domain.model.UserStat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
@Getter
@Setter
@ToString
public class UserStatRecord {

    public static String getPartitionKeyValue(UserId userId) {
        return "USER#" + userId.getValue();
    }

    public static UserStatRecord convertFrom(UserStat entity) {
        UserStatRecord record = new UserStatRecord();
        record.pk = UserRecord.getPartitionKeyValue(entity.getUser());
        record.sk = "USERSTAT";
        record.balance = entity.getBalance();
        record.numItems = entity.getNumItems();
        record.numShares = entity.getNumShares();
        record.numGiftsSent = entity.getNumGiftsSent();
        record.numGiftsReceived = entity.getNumGiftsReceived();
        return record;
    }

    private String pk;
    private String sk;
    private BigDecimal balance;
    private int numItems;
    private int numShares;
    private int numGiftsSent;
    private int numGiftsReceived;

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

    @DynamoDbAttribute("balance")
    public BigDecimal getBalance() {
        return balance;
    }

    @DynamoDbAttribute("numItems")
    public int getNumItems() {
        return numItems;
    }

    @DynamoDbAttribute("numShares")
    public int getNumShares() {
        return numShares;
    }

    @DynamoDbAttribute("numGiftsSent")
    public int getNumGiftsSent() {
        return numGiftsSent;
    }

    @DynamoDbAttribute("numGiftsReceived")
    public int getNumGiftsReceived() {
        return numGiftsReceived;
    }
}
