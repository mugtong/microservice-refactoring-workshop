package app.anne.give.controller;

import app.anne.give.application.GiveQueryService;
import app.anne.give.controller.dto.GiveResponse;
import app.anne.give.domain.model.ItemInQr;
import app.anne.give.domain.model.ItemOutQr;
import app.anne.give.application.WarehouseCommandService;
import app.anne.give.controller.dto.WarehouseCommandQrRequest;
import app.anne.give.controller.dto.WarehouseCommandResponse;
import app.anne.give.application.exception.WarehouseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WarehouseController {

    private final GiveQueryService giveQueryService;
    private final WarehouseCommandService warehouseCommandService;

    public WarehouseController(GiveQueryService giveQueryService,
                               WarehouseCommandService warehouseCommandService) {
        this.giveQueryService = giveQueryService;
        this.warehouseCommandService = warehouseCommandService;
    }

    @GetMapping(value = "/warehouse/incoming")
    public ResponseEntity<GiveResponse> getIncomingItemDetail(@RequestParam String qr) {
        return giveQueryService.findByItemInQr(new ItemInQr(qr))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/warehouse/incoming")
    public ResponseEntity<WarehouseCommandResponse> createIncomingItem(@RequestBody WarehouseCommandQrRequest request) {
        try {
            warehouseCommandService.createIncomingItem(new ItemInQr(request.getQr()), request.getIsbn());
        } catch (WarehouseException e) {
            return ResponseEntity.badRequest().body(new WarehouseCommandResponse(e.getErrorCode()));
        }
        return ResponseEntity.ok(new WarehouseCommandResponse());
    }

    @GetMapping(value = "/warehouse/outgoing")
    public ResponseEntity<GiveResponse> getOutgoingItemDetail(@RequestParam String qr) {
        return giveQueryService.findByItemOutQr(new ItemOutQr(qr))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/warehouse/outgoing")
    public ResponseEntity<WarehouseCommandResponse> createOutgoingItem(@RequestBody WarehouseCommandQrRequest request) {
        try {
            warehouseCommandService.createOutgoingItem(new ItemOutQr(request.getQr()), request.getIsbn());
        } catch (WarehouseException e) {
            return ResponseEntity.badRequest().body(new WarehouseCommandResponse(e.getErrorCode()));
        }
        return ResponseEntity.ok(new WarehouseCommandResponse());
    }
}
