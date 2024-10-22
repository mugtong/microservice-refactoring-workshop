package app.anne.place.application;

import app.anne.place.domain.PlaceRepository;
import app.anne.place.domain.model.Place;
import app.anne.place.domain.model.PlaceId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceQueryService {

    private final PlaceRepository placeRepository;

    public PlaceQueryService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> findPlaces(PlaceId lastPlaceId) {
        return placeRepository.findAll(lastPlaceId);
    }

    public Optional<Place> findPlaceById(PlaceId placeId) {
        return placeRepository.findById(placeId);
    }
}
