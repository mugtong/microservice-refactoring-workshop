package app.anne.warehouse.controller;

import app.anne.warehouse.application.WarehouseCommandService;
import app.anne.warehouse.application.WarehouseQueryService;
import app.anne.warehouse.controller.dto.GiveItemDetailResponse;
import app.anne.warehouse.controller.dto.WarehouseCommandQrRequest;
import app.anne.warehouse.controller.dto.WarehouseCommandResponse;
import app.anne.warehouse.exception.WarehouseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WarehouseController {

    private final WarehouseQueryService warehouseQueryService;
    private final WarehouseCommandService warehouseCommandService;

    public WarehouseController(WarehouseQueryService warehouseQueryService,
                               WarehouseCommandService warehouseCommandService) {
        this.warehouseQueryService = warehouseQueryService;
        this.warehouseCommandService = warehouseCommandService;
    }

    @GetMapping(value = "/warehouse/incoming")
    public ResponseEntity<GiveItemDetailResponse> getIncomingGiveItemDetail(@RequestParam String qr) {
        return warehouseQueryService.getIncomingGiveItemDetail(qr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/warehouse/incoming")
    public ResponseEntity<WarehouseCommandResponse> createIncomingGiveItem(@RequestBody WarehouseCommandQrRequest request) {
        try {
            warehouseCommandService.createIncomingGiveItem(request.getQr());
        } catch (WarehouseException e) {
            return ResponseEntity.badRequest().body(new WarehouseCommandResponse(e.getErrorCode()));
        }
        return ResponseEntity.ok(new WarehouseCommandResponse());
    }

    @GetMapping(value = "/warehouse/outgoing")
    public ResponseEntity<GiveItemDetailResponse> getOutgoingGiveItemDetail(@RequestParam String qr) {
        return warehouseQueryService.getOutgoingGiveItemDetail(qr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
