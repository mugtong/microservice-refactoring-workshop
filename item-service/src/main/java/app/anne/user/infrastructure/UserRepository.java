package app.anne.user.infrastructure;

import app.anne.user.domain.model.User;
import app.anne.user.domain.model.UserId;
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

@Repository
public class UserRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<UserRecord> userDynamoDbTable;

    public UserRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, @Value("${aws.dynamodb.table}") String tableName) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.userDynamoDbTable = this.dynamoDbEnhancedClient.table(tableName,
            TableSchema.fromBean(UserRecord.class));
    }

    public UserRecord findOne(UserId userId) {
        Key key = getPrimaryKey(userId);
        return userDynamoDbTable.getItem(key);
    }

    // public List<UserRecord> findAll(OwnerId ownerId) {
    //     Key partitionKey = Key.builder()
    //             .partitionValue(UserRecord.getPartitionKeyValue(ownerId))
    //             .build();
    //     QueryConditional queryConditional = QueryConditional.keyEqualTo(partitionKey);
    //     QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
    //         .queryConditional(queryConditional)
    //         .scanIndexForward(false)
    //         .build();

    //     PageIterable<UserRecord> pagedResults = ((PageIterable<UserRecord>) userDynamoDbTable.index("GSI1")
    //         .query(queryEnhancedRequest));

    //     return pagedResults.users().stream().toList();
    // }

    public UserRecord save(User userEntity) {
        UserRecord record = UserRecord.convertFrom(userEntity);
        userDynamoDbTable.putItem(record);
        return record;
    }

    public void removeUser(UserId userId) {
        Key key = getPrimaryKey(userId);
        userDynamoDbTable.deleteItem(key);
    }

    private Key getPrimaryKey(UserId userId) {
        return Key.builder()
                .partitionValue(UserRecord.getPartitionKeyValue(userId))
                .sortValue("USER")
                .build();
    }
}

