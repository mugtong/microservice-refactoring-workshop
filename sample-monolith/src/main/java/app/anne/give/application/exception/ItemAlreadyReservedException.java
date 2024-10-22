package app.anne.give.application.exception;

import app.anne.give.domain.exception.GiveException;
import app.anne.give.domain.model.GiveId;
import app.anne.item.domain.model.aggregates.ItemId;

public class ItemAlreadyReservedException extends GiveException {
    public ItemAlreadyReservedException(ItemId itemId, GiveId giveId) {
        super(
                String.format("item id %s is already reserved by give id %s",
                        itemId.getValue(),
                        giveId.getStringValue())
        );
    }
}
