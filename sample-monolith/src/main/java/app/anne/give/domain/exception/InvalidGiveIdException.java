package app.anne.give.domain.exception;

public class InvalidGiveIdException extends GiveException {
    public InvalidGiveIdException(String giveId) {
        super(String.format("invalid give id: %s", giveId));
    }
}