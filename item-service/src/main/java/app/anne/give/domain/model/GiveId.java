package app.anne.give.domain.model;

import app.anne.give.domain.exception.InvalidGiveIdException;
import com.github.f4b6a3.ulid.Ulid;

public class GiveId {
    private Ulid value;

    public GiveId(Ulid value) {
        this.value = value;
    }

    public static GiveId createFromStringValue(String strValue) {
        Ulid value;
        try {
            value = Ulid.from(strValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidGiveIdException(strValue);
        }
        return new GiveId(value);
    }

    public String getStringValue() {
        return this.value.toString();
    }

    @Override
    public String toString() {
        return getStringValue();
    }
}
