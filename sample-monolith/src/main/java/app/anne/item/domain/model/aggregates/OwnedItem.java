package app.anne.item.domain.model.aggregates;


import app.anne.user.domain.model.User;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class OwnedItem {
    private OwnerId owner;
    private final ItemId item;
    private OwnedItemStatus status;
    private String ownerName;
    private String ownerImageUrl;
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

    public static OwnedItemBuilder builderForCreateNew(OwnerId ownerId) {
        Ulid id = UlidCreator.getUlid();
        return hiddenBuilder()
                .owner(ownerId)
                .item(new ItemId(id.toString()))
                .status(OwnedItemStatus.PUBLIC)
                .type("BOOK")
                .updatedAt(Instant.now());
    }

    public static OwnedItemBuilder builderForExisting(OwnerId ownerId, ItemId itemId) {
        return hiddenBuilder()
                .owner(ownerId)
                .item(itemId);
    }

    public boolean isPublic() {
        return this.status == OwnedItemStatus.PUBLIC;
    }

    public void registerTo(User requester) {
        this.owner = new OwnerId(requester.getUser().getValue());
        this.ownerName = requester.getName();
        this.ownerImageUrl = requester.getPicture();
    }
}
