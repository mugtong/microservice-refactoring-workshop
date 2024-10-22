package app.anne.give.application.exception;

import lombok.Getter;

@Getter
public enum WarehouseErrorCode {
    NOT_EXISTS("give request not found"),
    INVALID_STATE("give request is in invalid state"),
    INVALID_USER("user not found"),
    INVALID_ITEM("item not found"),
    UNDEFINED("undefined");

    private final String description;

    WarehouseErrorCode(String description) {
        this.description = description;
    }
}
