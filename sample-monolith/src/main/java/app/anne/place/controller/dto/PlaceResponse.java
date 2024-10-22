package app.anne.place.controller.dto;

import app.anne.place.domain.model.Place;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlaceResponse {
    public static PlaceResponse convertFrom(Place entity) {
        PlaceResponse dto = new PlaceResponse();
        dto.setId(entity.getId().getValue());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setTelephone(entity.getTelephone());
        dto.setAddress(entity.getAddress().getFullAddress());
        dto.setRoadAddress(entity.getAddress().getRoadAddress());

        if (entity.getLocations() != null) {
            for (String location : entity.getLocations()) {
                dto.getLocations().add(location);
            }
        }

        if (!entity.getGeoLocation().isEmpty()) {
            dto.getGeoLocation().add(entity.getGeoLocation().getX());
            dto.getGeoLocation().add(entity.getGeoLocation().getY());
        }

        dto.setLink(entity.getLink());
        return dto;
    }

    private String id;
    private String name;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private List<String> locations = new ArrayList<>();
    private List<String> geoLocation = new ArrayList<>();
    private String link;
}
