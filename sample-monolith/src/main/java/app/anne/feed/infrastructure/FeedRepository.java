package app.anne.feed.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class FeedRepository {

    private static final int PAGE_SIZE = 10;
    private static final int ONE_PAGE = 1;

    private final DynamoDbTable<FeedRecord> feedTable;

    public FeedRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                          @Value("${aws.dynamodb.table}") String tableName) {
        this.feedTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(FeedRecord.class));
    }

    public Optional<FeedRecord> retrieveFeedWithId(String lastFeedId) {
        Key key = Key.builder()
                .partitionValue(FeedRecord.getPartitionKeyValue(lastFeedId))
                .sortValue(FeedRecord.getSortKeyValue())
                .build();
        FeedRecord record = feedTable.getItem(key);
        return Optional.ofNullable(record);
    }

    public List<FeedRecord> retrieveFeeds(FeedRecord lastFeedRecord) {
        final DynamoDbIndex<FeedRecord> index = feedTable.index("GSI2");
        final Key queryKey = Key.builder()
                .partitionValue(FeedRecord.getGsi2PartitionKeyValue())
                .build();

        SdkIterable<Page<FeedRecord>> pagedResult;
        if (lastFeedRecord == null) {
            pagedResult = index.query(
                    r -> r.queryConditional(QueryConditional.keyEqualTo(queryKey))
                            .scanIndexForward(false)
                            .limit(PAGE_SIZE));
        } else {
            final Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("PK", AttributeValue.builder()
                    .s(lastFeedRecord.getPk())
                    .build());
            exclusiveStartKey.put("SK", AttributeValue.builder()
                    .s(lastFeedRecord.getSk())
                    .build());
            exclusiveStartKey.put("GSI2_PK", AttributeValue.builder()
                    .s(lastFeedRecord.getGsi2Pk())
                    .build());
            exclusiveStartKey.put("GSI2_SK", AttributeValue.builder()
                    .s(lastFeedRecord.getGsi2Sk())
                    .build());

            pagedResult = index.query(
                    r -> r.queryConditional(QueryConditional.keyEqualTo(queryKey))
                            .scanIndexForward(false)
                            .limit(PAGE_SIZE)
                            .exclusiveStartKey(exclusiveStartKey));
        }

        return pagedResult.stream()
                .limit(ONE_PAGE)
                .flatMap(page -> page.items().stream())
                .toList();
    }
}
