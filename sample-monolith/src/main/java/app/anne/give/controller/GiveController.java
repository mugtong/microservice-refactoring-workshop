package app.anne.give.controller;

import app.anne.give.application.GiveCommandService;
import app.anne.give.application.GiveQueryService;
import app.anne.give.application.ItemQrService;
import app.anne.give.application.exception.InvalidDeleteGiveRequestException;
import app.anne.give.controller.dto.*;
import app.anne.give.domain.exception.GiveException;
import app.anne.give.domain.exception.InvalidGiveIdException;
import app.anne.give.domain.model.RequesterId;
import app.anne.item.application.exception.InvalidItemException;
import app.anne.give.domain.model.GiveId;
import app.anne.item.domain.model.aggregates.OwnerId;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GiveController {
    private final GiveCommandService giveCommandService;
    private final GiveQueryService giveQueryService;
    private final ItemQrService itemQrService;

    public GiveController(GiveQueryService giveQueryService,
                          GiveCommandService giveCommandService,
                          ItemQrService itemQrService) {
        this.giveQueryService = giveQueryService;
        this.giveCommandService = giveCommandService;
        this.itemQrService = itemQrService;
    }

    @GetMapping(value = "/owner/{userId}/give")
    public List<GiveResponse> getOwnerGiveRequest(@PathVariable("userId") String ownerId) {
        return giveQueryService.findByOwner(new OwnerId(ownerId));
    }

    @GetMapping(value = "/owner/{userId}/give/{giveId}")
    public ResponseEntity<?> getOwnerGiveRequest(@PathVariable("userId") String ownerId,
                                                @PathVariable("giveId") String giveId) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(giveId);
        } catch (InvalidGiveIdException e) {
            return giveNotFound();
        }
        return giveQueryService.findOneByOwner(new OwnerId(ownerId), parsedGiveId)
                .map(ResponseEntity::ok)
                .orElse(giveNotFound());
    }

    @GetMapping(value = "/owner/{userId}/give/{giveId}/itemInQr")
    public ResponseEntity<?> getItemInQrForOwner(@PathVariable("userId") String ownerId,
                                                 @PathVariable("giveId") String giveId) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(giveId);
        } catch (InvalidGiveIdException e) {
            return giveNotFound();
        }
        return itemQrService.getItemInQrForOwner(new OwnerId(ownerId), parsedGiveId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/requester/{userId}/give")
    public List<GiveResponse> getRequesterGiveRequest(@PathVariable("userId") String requesterId) {
        return giveQueryService.findByRequester(new RequesterId(requesterId));
    }

    @GetMapping(value = "/requester/{userId}/give/{giveId}")
    public ResponseEntity<?> getRequesterGiveRequest(@PathVariable("userId") String requesterId,
                                                    @PathVariable("giveId") String giveId) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(giveId);
        } catch (InvalidGiveIdException e) {
            return giveNotFound();
        }
        return giveQueryService.findOneByRequester(new RequesterId(requesterId), parsedGiveId)
                .map(ResponseEntity::ok)
                .orElse(giveNotFound());
    }

    @GetMapping(value = "/requester/{userId}/give/{giveId}/itemOutQr")
    public ResponseEntity<?> getItemOutQrForRequester(@PathVariable("userId") String requesterId,
                                                      @PathVariable("giveId") String giveId) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(giveId);
        } catch (InvalidGiveIdException e) {
            return giveNotFound();
        }
        return itemQrService.getItemOutQrForRequester(new RequesterId(requesterId), parsedGiveId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/give")
    public ResponseEntity<?> createGiveRequest(@RequestBody @Valid CreateGiveRequest request) {
        GiveId createdId;
        try {
            createdId = giveCommandService.createRequest(request);
        } catch (InvalidItemException e) {
            return badRequest(e.getMessage());
        }
        GiveResponse response = new GiveResponse();
        response.setGiveId(createdId.getStringValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/give")
    public ResponseEntity<?> deleteGiveRequest(@RequestBody @Valid DeleteGiveRequest request) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(request.getGiveId());
        } catch (InvalidGiveIdException e) {
            return badRequest("not found");
        }
        try {
            giveCommandService.deleteRequest(parsedGiveId, new OwnerId(request.getOwnerId()),
                    new RequesterId(request.getRequesterId()));
        } catch (InvalidDeleteGiveRequestException e) {
            return badRequest(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/give/accept")
    public ResponseEntity<?> acceptGiveRequest(@RequestBody @Valid AcceptGiveRequest request) {
        GiveId parsedGiveId;
        try {
            parsedGiveId = GiveId.createFromStringValue(request.getGiveId());
        } catch (InvalidGiveIdException e) {
            return ResponseEntity.badRequest().body(new GiveCommandResponse("not found"));
        }
        try {
            giveCommandService.acceptRequest(parsedGiveId, new OwnerId(request.getOwnerId()));
        } catch (GiveException e) {
            return ResponseEntity.badRequest().body(new GiveCommandResponse(e.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<ErrorResponse> badRequest(String message) {
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<GiveResponse> giveNotFound() {
        return ResponseEntity.ok(new GiveResponse());
    }
}
