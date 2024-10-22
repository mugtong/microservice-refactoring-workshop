package app.anne.place.domain;

import app.anne.place.domain.model.Place;
import app.anne.place.domain.model.PlaceId;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository {

    Optional<Place> findById(PlaceId id);

    List<Place> findAll(PlaceId lastRetrievedId);
}
