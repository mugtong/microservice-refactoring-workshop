package app.anne.item.domain.model.aggregates;

import app.anne.place.domain.model.Place;
import app.anne.user.domain.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ItemHistory {

    public static ItemHistory createItemCreatedHistory(ItemId itemId, User owner) {
        ItemHistory history = new ItemHistory();
        history.setItemId(itemId);
        history.setOwnerId(new OwnerId(owner.getUser().getValue()));
        history.setOwnerName(owner.getName());
        history.setLocations(owner.getLocations());
        return history;
    }

    public static ItemHistory createItemTransferHistory(ItemId itemId, User newOwner, Place place) {
        ItemHistory history = new ItemHistory();
        history.setItemId(itemId);
        history.setOwnerId(new OwnerId(newOwner.getUser().getValue()));
        history.setOwnerName(newOwner.getName());
        history.setLocations(place.getLocations());
        return history;
    }

    private ItemId itemId;
    private OwnerId ownerId;
    private String ownerName;
    private List<String> locations;

    private void setLocations(List<String> locations) {
        this.locations = locations == null ? new ArrayList<>() : new ArrayList<>(locations);
    }
}
