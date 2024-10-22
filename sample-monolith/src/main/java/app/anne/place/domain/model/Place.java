package app.anne.place.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Place {
    public static Place unknown(PlaceId id) {
        return Place.builder()
                .id(id)
                .name("UNKNOWN")
                .build();
    }

    private PlaceId id;
    private String name;
    private String category;
    private String description;
    private String telephone;
    private Address address;
    private List<String> locations;
    private GeoLocation geoLocation;
    private String link;
}
