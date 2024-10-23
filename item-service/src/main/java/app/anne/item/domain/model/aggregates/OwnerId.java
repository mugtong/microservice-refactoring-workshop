package app.anne.item.domain.model.aggregates;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OwnerId {
    private String value;

    @Override
    public String toString() {
        return value;
    }
}
