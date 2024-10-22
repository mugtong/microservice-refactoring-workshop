package app.anne.place.infrastructure;

import app.anne.place.domain.PlaceRepository;
import app.anne.place.domain.model.Place;
import app.anne.place.domain.model.PlaceId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

    private static final int PAGE_SIZE = 10;
    private static final int ONE_PAGE = 1;

    private final DynamoDbTable<PlaceRecord> placeTable;

    public PlaceRepositoryImpl(DynamoDbEnhancedClient client,
                              @Value("${aws.dynamodb.table}") String tableName) {
        this.placeTable = client.table(tableName, TableSchema.fromBean(PlaceRecord.class));
    }
    @Override
    public Optional<Place> findById(PlaceId id) {
        Key key = Key.builder()
                .partitionValue(PlaceRecord.getPartitionKeyValue(id))
                .sortValue(PlaceRecord.getSortKeyValue())
                .build();
        PlaceRecord record = placeTable.getItem(key);
        return Optional.ofNullable(record)
                .map(PlaceRecord::convertToEntity);
    }

    @Override
    public List<Place> findAll(PlaceId lastRetrievedId) {
        final Key queryKey = Key.builder()
                .partitionValue(PlaceRecord.getSortKeyValue())
                .build();
        final DynamoDbIndex<PlaceRecord> index = placeTable.index("Inverted");

        final SdkIterable<Page<PlaceRecord>> pagedResult;
        if (lastRetrievedId == null) {
            pagedResult = index.query(
                    r -> r.queryConditional(QueryConditional.keyEqualTo(queryKey))
                            .limit(PAGE_SIZE)
            );

        } else {
            final Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("SK", AttributeValue.builder()
                    .s(PlaceRecord.getSortKeyValue())
                    .build());
            exclusiveStartKey.put("PK", AttributeValue.builder()
                    .s(PlaceRecord.getPartitionKeyValue(lastRetrievedId))
                    .build());

            pagedResult = index.query(
                    r -> r.queryConditional(QueryConditional.keyEqualTo(queryKey))
                            .limit(PAGE_SIZE)
                            .exclusiveStartKey(exclusiveStartKey)
            );
        }

        return pagedResult.stream()
                .limit(ONE_PAGE)
                .flatMap(page -> page.items().stream())
                .map(PlaceRecord::convertToEntity)
                .toList();
    }
}
