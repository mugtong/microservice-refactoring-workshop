package app.anne.place.controller;

import app.anne.place.application.PlaceQueryService;
import app.anne.place.controller.dto.PlaceResponse;
import app.anne.place.domain.model.Place;
import app.anne.place.domain.model.PlaceId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PlaceController {

    private final PlaceQueryService placeQueryService;

    public PlaceController(PlaceQueryService placeQueryService) {
        this.placeQueryService = placeQueryService;
    }

    @GetMapping(value = "/place")
    public List<PlaceResponse> getPlaces(@RequestParam(name = "lastId", required = false) String lastId) {
        List<Place> result = placeQueryService.findPlaces(lastId == null ? null : new PlaceId(lastId));
        return result.stream()
                .map(PlaceResponse::convertFrom)
                .toList();
    }

    @GetMapping(value = "/place/{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable("id") String placeId) {
        Optional<Place> place = placeQueryService.findPlaceById(new PlaceId(placeId));
        return ResponseEntity.of(place.map(PlaceResponse::convertFrom));
    }
}
