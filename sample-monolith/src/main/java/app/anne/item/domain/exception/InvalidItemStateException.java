package app.anne.item.domain.exception;

import app.anne.item.domain.model.aggregates.OwnedItemStatus;

public class InvalidItemStateException extends RuntimeException {
    public InvalidItemStateException(OwnedItemStatus status) {
        super(String.format("Invalid item state: %s", status.name()));
    }
}
