package app.anne.place.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Address {
    private String fullAddress;
    private String roadAddress;
}
