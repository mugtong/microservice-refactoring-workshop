package app.anne.item.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateItemResource {
    private String ownerId;
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
}
