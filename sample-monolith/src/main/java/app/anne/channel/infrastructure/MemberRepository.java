package app.anne.channel.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.Member;
import app.anne.channel.domain.model.MemberId;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;

@Repository
public class MemberRepository {
    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<MemberRecord> memberTable;

    public MemberRepository(DynamoDbEnhancedClient client,
                              @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.memberTable = this.client.table(tableName, TableSchema.fromBean(MemberRecord.class));
    }

    public MemberRecord findMember(ChannelId channelId, MemberId memberId) {
        Key key = getPrimaryKey(channelId, memberId);
        return memberTable.getItem(key);
    }

    public MemberRecord createMember(Member member) {
        MemberRecord record = MemberRecord.convertFrom(member);
        memberTable.putItem(record);
        return record; 
    }

    public List<MemberRecord> getChannelMembers(ChannelId channelId) {
        Key key = Key.builder().partitionValue(MemberRecord.getPartitionKeyValue(channelId)).sortValue("MEMBER#").build();
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(key);
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<MemberRecord> pagedResults = ((PageIterable<MemberRecord>) memberTable
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    public long getChannelSize(ChannelId channelId) {
        Key key = Key.builder().partitionValue(MemberRecord.getPartitionKeyValue(channelId)).sortValue("MEMBER#").build();
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(key);
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .addAttributeToProject("PK")
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        return memberTable.query(queryEnhancedRequest).stream().count();
    }

    private Key getPrimaryKey(ChannelId channelId, MemberId memberId) {
        return Key.builder()
                .partitionValue(MemberRecord.getPartitionKeyValue(channelId))
                .sortValue(MemberRecord.getSortKeyValue(memberId))
                .build();
    }
}
