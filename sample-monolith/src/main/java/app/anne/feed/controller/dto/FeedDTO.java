package app.anne.feed.controller.dto;

import app.anne.feed.domain.model.FeedType;
import app.anne.feed.infrastructure.FeedRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedDTO {
    private String feedId;
    private Instant updatedAt;
    private FeedType type;
    private String ownerId;
    private String ownerName;
    private String ownerImageUrl;
    private String itemId;
    private String itemImageUrl;
    private String title;
    private String author;
    private List<String> locations;

    public FeedDTO(FeedRecord feed) {
        this.feedId = feed.getItemId();
        this.updatedAt = feed.getUpdatedAt();
        this.type = feed.getType();
        this.ownerId = feed.getOwnerId();
        this.ownerName = feed.getOwnerName();
        this.ownerImageUrl = feed.getOwnerImageUrl();
        this.itemId = feed.getItemId();
        this.itemImageUrl = feed.getItemImageUrl();
        this.title = feed.getTitle();
        this.author = feed.getAuthor();
    }
}
