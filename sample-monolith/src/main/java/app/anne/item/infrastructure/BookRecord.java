package app.anne.item.infrastructure;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;
import java.util.Map;

@DynamoDbBean
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRecord {

    public static String getPartitionKeyValue(String isbn) {
        return String.format("BOOK#%s", isbn);
    }

    public static String getSortKeyValue() {
        return "BOOK";
    }

    private String pk;
    private String sk;
    private Instant createdAt;
    private String title;
    private String subTitle;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String isbn;
    private String isbn13;
    private Integer itemId;
    private Integer priceSales;
    private Integer priceStandard;
    private String mallType;
    private String stockStatus;
    private Integer mileage;
    private String imageUrl;
    private String cover;
    private Integer categoryId;
    private String categoryName;
    private String publisher;
    private Integer salesPoint;
    private String adult;
    private String fixedPrice;
    private String customerReviewRank;
    private Map<String, String> subInfo;

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

    @DynamoDbAttribute("ct")
    public Instant getCreatedAt() {
        return createdAt;
    }
}


