package app.anne.warehouse.controller.dto;

import app.anne.warehouse.exception.WarehouseErrorCode;
import lombok.Data;

@Data
public class WarehouseCommandResponse {
    private boolean error;
    private String code;

    public WarehouseCommandResponse() {
        this.error = false;
        this.code = null;
    }

    public WarehouseCommandResponse(WarehouseErrorCode code) {
        this.error = true;
        this.code = code.getValue();
    }
}
