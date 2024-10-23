package app.anne.warehouse.exception;

import lombok.Getter;

@Getter
public enum WarehouseErrorCode {
    NOT_EXISTS("E001", "item does not exist"),
    INVALID_STATE("E003", "the item is in invalid state"),
    UNDEFINED("E999", "undefined error");

    private final String value;
    private final String description;

    WarehouseErrorCode(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
