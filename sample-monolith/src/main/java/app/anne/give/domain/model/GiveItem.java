package app.anne.give.domain.model;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GiveItem {
    private ItemId id;
    private OwnerId ownerId;
    private String isbn;
    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
}
