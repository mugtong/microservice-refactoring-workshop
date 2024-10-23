package app.anne.give.domain.exception;

import app.anne.give.domain.model.Give;

public class InvalidGiveStateException extends RuntimeException {
    public InvalidGiveStateException(Give.Status status) {
        super(String.format("Invalid status: %s", status.name()));
    }
}
