package app.anne.item.infrastructure;

import app.anne.item.controller.dto.Book;

import java.time.Instant;

public class BookRecordMapper {
    public static Book convertToModel(BookRecord record) {
        return Book.builder()
                .title(record.getTitle())
                .subTitle(record.getSubTitle())
                .link(record.getLink())
                .author(record.getAuthor())
                .pubDate(record.getPubDate())
                .description(record.getDescription())
                .isbn(record.getIsbn())
                .isbn13(record.getIsbn13())
                .itemId(record.getItemId())
                .priceSales(record.getPriceSales())
                .priceStandard(record.getPriceStandard())
                .mallType(record.getMallType())
                .stockStatus(record.getStockStatus())
                .mileage(record.getMileage())
                .imageUrl(record.getImageUrl())
                .cover(record.getCover())
                .categoryId(record.getCategoryId())
                .categoryName(record.getCategoryName())
                .publisher(record.getPublisher())
                .salesPoint(record.getSalesPoint())
                .adult(record.getAdult())
                .fixedPrice(record.getFixedPrice())
                .customerReviewRank(record.getCustomerReviewRank())
                .subInfo(record.getSubInfo())
                .build();
    }

    public static BookRecord convertFromModel(Book book) {
        return BookRecord.builder()
                .pk(BookRecord.getPartitionKeyValue(book.getIsbn13()))
                .sk(BookRecord.getSortKeyValue())
                .createdAt(Instant.now())
                .title(book.getTitle())
                .subTitle(book.getSubTitle())
                .link(book.getLink())
                .author(book.getAuthor())
                .pubDate(book.getPubDate())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .isbn13(book.getIsbn13())
                .itemId(book.getItemId())
                .priceSales(book.getPriceSales())
                .priceStandard(book.getPriceStandard())
                .mallType(book.getMallType())
                .stockStatus(book.getStockStatus())
                .mileage(book.getMileage())
                .imageUrl(book.getImageUrl())
                .cover(book.getCover())
                .categoryId(book.getCategoryId())
                .categoryName(book.getCategoryName())
                .publisher(book.getPublisher())
                .salesPoint(book.getSalesPoint())
                .adult(book.getAdult())
                .fixedPrice(book.getFixedPrice())
                .customerReviewRank(book.getCustomerReviewRank())
                .subInfo(book.getSubInfo())
                .build();
    }
}
