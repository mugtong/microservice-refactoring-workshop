package app.anne.give.application.exception;

import app.anne.give.domain.exception.GiveException;

public class InvalidDeleteGiveRequestException extends GiveException {
    public InvalidDeleteGiveRequestException(String message) {
        super(message);
    }
}
