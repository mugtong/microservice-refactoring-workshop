package app.anne.give.application.exception;

public class WarehouseException extends RuntimeException {

    private final WarehouseErrorCode errorCode;

    public WarehouseException(WarehouseErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WarehouseErrorCode getErrorCode() {
        return this.errorCode;
    }
}
