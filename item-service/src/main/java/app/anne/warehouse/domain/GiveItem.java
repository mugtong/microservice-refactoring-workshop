package app.anne.warehouse.domain;

import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.model.RequesterId;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnedItemStatus;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.place.domain.model.PlaceId;
import app.anne.warehouse.exception.WarehouseErrorCode;
import app.anne.warehouse.exception.WarehouseException;

public class GiveItem {
    private final Give give;
    private final OwnedItem item;

    public GiveItem(Give give, OwnedItem item) {
        this.give = give;
        this.item = item;
    }

    public boolean validIncoming() {
        return give.isAccepted() && item.isPendingReceive();
    }

    public void receive() {
        if (!validIncoming()) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_STATE);
        }
        this.item.receive();
    }

    public boolean validOutgoing() {
        return give.isAccepted() && item.isReceived();
    }

    public GiveId getGiveId() {
        return give.getId();
    }

    public Give.Status getGiveStatus() {
        return give.getStatus();
    }

    public OwnerId getOwnerId() {
        return give.getOwner();
    }

    public RequesterId getRequesterId() {
        return give.getRequester();
    }

    public OwnedItem getItem() {
        return item;
    }

    public ItemId getItemId() {
        return item.getItem();
    }

    public String getItemIsbn() {
        return item.getIsbn();
    }

    public String getItemTitle() {
        return item.getTitle();
    }

    public String getItemAuthor() {
        return item.getAuthor();
    }

    public String getItemPublisher() {
        return item.getPublisher();
    }

    public String getItemImageUrl() {
        return item.getImageUrl();
    }

    public OwnedItemStatus getItemStatus() {
        return item.getStatus();
    }

    public PlaceId getPlaceId() {
        return give.getPlace();
    }
}
