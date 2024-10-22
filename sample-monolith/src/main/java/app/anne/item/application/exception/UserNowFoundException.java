package app.anne.item.application.exception;

import app.anne.user.domain.model.UserId;

public class UserNowFoundException extends RuntimeException {
    public UserNowFoundException(UserId id) {
        super(String.format("user of id '%s' not found", id.getValue()));
    }
}
