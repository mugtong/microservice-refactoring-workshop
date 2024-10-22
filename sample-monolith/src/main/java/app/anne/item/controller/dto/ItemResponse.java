package app.anne.item.controller.dto;

import app.anne.item.infrastructure.ItemRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private String itemId;
    private String ownerId;
    private String status;
    private String adult;
    private String author;
    private String categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
    private String isbn;
    private String isbn13;
    private String link;
    private String pubDate;
    private String publisher;
    private String subTitle;
    private String title;
    private String type;
    private Instant updatedAt;

    public ItemResponse(ItemRecord item) {
        this.itemId = item.getItemId();
        this.ownerId = item.getOwner();
        this.status = item.getStatus();
        this.adult = item.getAdult();
        this.author = item.getAuthor();
        this.categoryId = item.getCategoryId();
        this.categoryName = item.getCategoryName();
        this.description = item.getDescription();
        this.imageUrl = item.getImageUrl();
        this.isbn = item.getIsbn();
        this.isbn13 = item.getIsbn13();
        this.link = item.getLink();
        this.pubDate = item.getPubDate();
        this.publisher = item.getPublisher();
        this.subTitle = item.getSubTitle();
        this.title = item.getTitle();
        this.type = item.getType();
        this.updatedAt = item.getUpdatedAt();
    }
}
