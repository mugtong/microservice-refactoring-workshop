package app.anne.channel.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import app.anne.channel.domain.model.Channel;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.repository.ChannelRepository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;

@Repository
public class ChannelRepositoryImpl implements ChannelRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<ChannelRecord> channelTable;

    public ChannelRepositoryImpl(DynamoDbEnhancedClient client,
                              @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.channelTable = this.client.table(tableName, TableSchema.fromBean(ChannelRecord.class));
    }

    @Override
    public ChannelRecord findOne(ChannelId channelId) {
        Key key = getPrimaryKey(channelId);
        return channelTable.getItem(key);
    }

    @Override
    public List<ChannelRecord> findAll() {
        QueryConditional queryConditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue("CHANNEL")
                .build());
        QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false)
            .build();

        PageIterable<ChannelRecord> pagedResults = ((PageIterable<ChannelRecord>) channelTable.index("GSI1")
            .query(queryEnhancedRequest));

        return pagedResults.items().stream().toList();
    }

    // @Override
    // public List<ChannelRecord> findAllByOpptyId(String opptyId, String sk) {
    //     QueryConditional queryConditional = QueryConditional
    //         .keyEqualTo(Key.builder().partitionValue("CHANNEL")
    //             .build());
    //     Expression expression = Expression.builder()
    //         .expression("#field = :value")
    //         .expressionNames(Map.of("#field", "Opportunity ID"))
    //         .expressionValues(Map.of(":value", AttributeValue.fromS(opptyId)))
    //         .build();

    //     QueryEnhancedRequest queryEnhancedRequest= QueryEnhancedRequest.builder()
    //         .queryConditional(queryConditional)
    //         .filterExpression(expression)
    //         .scanIndexForward(false)
    //         .build();

    //     PageIterable<ChannelRecord> pagedResults = ((PageIterable<ChannelRecord>) channelTable.index("GSI1")
    //         .query(queryEnhancedRequest));

    //     return pagedResults.items().stream().toList();
    // }    

    @Override
    public ChannelRecord save(Channel channel) {
        ChannelRecord record = ChannelRecord.convertFrom(channel);
        channelTable.putItem(record);
        return record;
    }

    @Override
    public void removeItem(ChannelId channelId) {
        Key key = getPrimaryKey(channelId);
        channelTable.deleteItem(key);
    }

    private Key getPrimaryKey(ChannelId channelId) {
        return Key.builder()
                .partitionValue(ChannelRecord.getPartitionKeyValue(channelId))
                .sortValue("CHANNEL")
                .build();
    }
}

