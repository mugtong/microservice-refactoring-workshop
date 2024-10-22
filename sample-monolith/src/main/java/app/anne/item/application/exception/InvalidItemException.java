package app.anne.item.application.exception;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;

public class InvalidItemException extends RuntimeException {

    public InvalidItemException(ItemId itemId) {
        super(String.format("item %s doesn't exist", itemId.getValue()));
    }
    public InvalidItemException(ItemId itemId, OwnerId ownerId) {
        super(String.format("item %s doesn't belong to owner %s", itemId.getValue(), ownerId.getValue()));
    }
}
