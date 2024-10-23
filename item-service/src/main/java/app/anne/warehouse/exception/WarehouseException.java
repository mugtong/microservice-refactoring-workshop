package app.anne.warehouse.exception;

public class WarehouseException extends RuntimeException {

    private final WarehouseErrorCode errorCode;

    public WarehouseException(WarehouseErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WarehouseErrorCode getErrorCode() {
        return this.errorCode;
    }
}
