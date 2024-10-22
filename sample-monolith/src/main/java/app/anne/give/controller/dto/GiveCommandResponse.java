package app.anne.give.controller.dto;

import app.anne.give.application.exception.WarehouseErrorCode;
import lombok.Data;

@Data
public class GiveCommandResponse {
    private boolean error;
    private String message;

    public GiveCommandResponse() {
        this.error = false;
        this.message = null;
    }

    public GiveCommandResponse(String message) {
        this.error = true;
        this.message = message;
    }
}
