package app.anne.give.controller.dto;

import app.anne.give.application.exception.WarehouseErrorCode;
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
        this.code = code.name();
    }
}
