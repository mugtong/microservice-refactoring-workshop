package app.anne.item.infrastructure;

import app.anne.item.controller.dto.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class BookInternalRepository {

    private final DynamoDbEnhancedClient client;
    private final DynamoDbTable<BookRecord> bookTable;

    public BookInternalRepository(DynamoDbEnhancedClient client,
                                  @Value("${aws.dynamodb.table}") String tableName) {
        this.client = client;
        this.bookTable = client.table(tableName, TableSchema.fromBean(BookRecord.class));
    }

    public List<Book> findBookByIsbn(String isbn) {
        Key key = getPrimaryKey(isbn);
        BookRecord record = bookTable.getItem(key);

        if (record == null) {
            return Collections.emptyList();
        }

        Book book = BookRecordMapper.convertToModel(record);
        return Collections.singletonList(book);
    }

    public void updateBooks(List<Book> books) {
        final WriteBatch.Builder<BookRecord> requestBuilder =
                WriteBatch.builder(BookRecord.class)
                .mappedTableResource(bookTable);

        for (Book book : books) {
            final BookRecord record = BookRecordMapper.convertFromModel(book);
            requestBuilder.addPutItem(b -> b.item(record));
        }

        BatchWriteResult result = client.batchWriteItem(b -> b.addWriteBatch(requestBuilder.build()));

        List<String> unprocessed = result.unprocessedPutItemsForTable(bookTable)
                .stream().map(BookRecord::getIsbn13)
                .toList();
        log.warn("Unprocessed: {}", unprocessed);
    }

    private Key getPrimaryKey(String isbn) {
        return Key.builder()
                .partitionValue(BookRecord.getPartitionKeyValue(isbn))
                .sortValue(BookRecord.getSortKeyValue())
                .build();
    }
}
