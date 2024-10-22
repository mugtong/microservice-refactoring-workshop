package app.anne.channel.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.Message;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;

@Repository
public class MessageRepository {
    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<MessageRecord> messageTable;

    public MessageRepository(DynamoDbEnhancedClient client, @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.messageTable = this.client.table(tableName, TableSchema.fromBean(MessageRecord.class));
    }

    public List<MessageRecord> loadMessages(ChannelId channelId, int size, int page) {
        QueryConditional queryConditional = QueryConditional
            .sortBeginsWith(Key.builder().partitionValue(ChannelRecord.getPartitionKeyValue(channelId)).sortValue("MESSAGE#")
            .build());
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .limit(size)
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<MessageRecord> pagedResults = ((PageIterable<MessageRecord>) messageTable
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    public MessageRecord createMessage(Message message) {
        MessageRecord record = MessageRecord.convertFrom(message);
        messageTable.putItem(record);
        return record;
    }

    public String getLastMessage(ChannelId channelId) {
        QueryConditional queryConditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(ChannelRecord.getPartitionKeyValue(channelId))
                .build());
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .limit(1)
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        return messageTable.query(queryEnhancedRequest).items().stream().findFirst().orElse(null).getBody();
    }
}
