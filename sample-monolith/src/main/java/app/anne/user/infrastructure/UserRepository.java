package app.anne.user.infrastructure;

import app.anne.user.domain.model.User;
import app.anne.user.domain.model.UserId;
import app.anne.user.domain.model.UserStat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserRepository {
    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<UserRecord> userTable;
    private final DynamoDbTable<UserStatRecord> userStatTable;

    public UserRepository(DynamoDbEnhancedClient client, @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.userTable = this.client.table(tableName,
            TableSchema.fromBean(UserRecord.class));
        this.userStatTable = this.client.table(tableName,
            TableSchema.fromBean(UserStatRecord.class));
    }

    public Optional<User> findOneById(UserId userId) {
        UserRecord record = getItem(userId);
        return Optional.ofNullable(record)
                .map(this::convertRecordToEntity);
    }

    private UserRecord getItem(UserId userId) {
        Key key = getPrimaryKey(userId);
        return userTable.getItem(key);
    }

    private User convertRecordToEntity(UserRecord record) {
        return User.builderForExisting(new UserId(record.getUserId()))
                .email(record.getEmail())
                .name(record.getName())
                .mobile(record.getMobile())
                .description(record.getDescription())
                .picture(record.getPicture())
                .locations(record.getLocations() == null ? new ArrayList<>() : new ArrayList<>(record.getLocations()))
                .build();
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

    //     PageIterable<UserRecord> pagedResults = ((PageIterable<UserRecord>) userTable.index("GSI1")
    //         .query(queryEnhancedRequest));

    //     return pagedResults.users().stream().toList();
    // }

    public UserRecord findByUserId(UserId userId) {
        Key key = getPrimaryKey(userId);
        return userTable.getItem(key);
    }

    public UserStatRecord findStatByUserId(UserId userId) {
        Key key = Key.builder()
                .partitionValue(UserStatRecord.getPartitionKeyValue(userId))
                .sortValue("USERSTAT")
                .build();
        return userStatTable.getItem(key);
    }

    /*
     * 유저가 만들어질때마다,
     * 유저 엔티티 (기본정보: 이메일, 사진, 이름 등) 생성
     * 유저 스탯 엔티티 생성 (숫자정보: 충전 잔액, 보유아이템 갯수, 나늠 횟수, 보낸/받은 선물 갯수)
     */
    public UserRecord saveNew(User userEntity) {
        final UserRecord userRecord = UserRecord.convertFrom(userEntity);
        final UserStatRecord userStatRecord = UserStatRecord.convertFrom(
            UserStat.builderForCreateNew(userEntity.getUser()).build());
        client.transactWriteItems(b -> b
                .addPutItem(userTable, userRecord)
                .addPutItem(userStatTable, userStatRecord)
        );
        return userRecord;
    }

    public UserStatRecord saveStat(UserStat userStatEntity) {
        UserStatRecord record = UserStatRecord.convertFrom(userStatEntity);
        userStatTable.putItem(record);
        return record;
    }

    public UserRecord updateUser(UserRecord newUserRecord) {        
        userTable.updateItem(newUserRecord);
        return newUserRecord;
    }

    public void deleteUser(UserId userId) {
        Key key = getPrimaryKey(userId);
        userTable.deleteItem(key);
    }

    private Key getPrimaryKey(UserId userId) {
        return Key.builder()
                .partitionValue(UserRecord.getPartitionKeyValue(userId))
                .sortValue("USER")
                .build();
    }
}

