package app.anne.place.domain.model;

import lombok.Getter;

@Getter
public class GeoLocation {

    public enum Type {
        EMPTY,
        TM128
    }

    public static GeoLocation createNullObject() {
        return new GeoLocation(Type.EMPTY, null, null);
    }

    private final Type type;
    private final String x;
    private final String y;

    public GeoLocation(Type type, String x, String y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        return type == Type.EMPTY;
    }
}
