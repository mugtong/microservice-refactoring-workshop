package app.anne.item.domain.model.aggregates;


import app.anne.item.domain.exception.InvalidItemStateException;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class OwnedItem {
    private final OwnerId owner;
    private final ItemId item;
    private OwnedItemStatus status;
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
                .status(OwnedItemStatus.REGISTERED)
                .updatedAt(Instant.now());
    }

    public static OwnedItemBuilder builderForExisting(OwnerId ownerId, ItemId itemId) {
        return hiddenBuilder()
                .owner(ownerId)
                .item(itemId);
    }

    public void pendingReceive() {
        if (this.status != OwnedItemStatus.REGISTERED) {
            throw new InvalidItemStateException(this.status);
        }
        this.status = OwnedItemStatus.PENDING_RECEIVE;
    }

    public void receive() {
        if (this.status != OwnedItemStatus.PENDING_RECEIVE) {
            throw new InvalidItemStateException(this.status);
        }
        this.status = OwnedItemStatus.RECEIVED;
    }

    public boolean isPendingReceive() {
        return this.status == OwnedItemStatus.PENDING_RECEIVE;
    }

    public boolean isReceived() {
        return this.status == OwnedItemStatus.RECEIVED;
    }
}
