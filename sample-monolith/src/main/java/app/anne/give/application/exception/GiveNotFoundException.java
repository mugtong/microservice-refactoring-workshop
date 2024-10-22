package app.anne.give.application.exception;

import app.anne.give.domain.exception.GiveException;
import app.anne.give.domain.model.GiveId;
import app.anne.item.domain.model.aggregates.OwnerId;

public class GiveNotFoundException extends GiveException {
    public GiveNotFoundException(GiveId giveId, OwnerId ownerId) {
        super(
                String.format("give id %s of owner %s not found",
                    giveId.getStringValue(),
                    ownerId.getValue())
        );
    }
}
