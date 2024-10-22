package app.anne.item.controller.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
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
}
