package app.anne.give.domain.model;

import app.anne.give.domain.exception.InvalidGiveStateException;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.place.domain.model.PlaceId;
import lombok.Getter;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import java.time.Instant;
import java.util.Optional;


@Getter
public class Give {

    public enum Status {
        REQUESTED,
        ACCEPTED,
        CANCELED,
        COMPLETED;
    }

    public static Give createNewInstance(ItemId item, OwnerId owner, RequesterId requester, PlaceId place) {
        Ulid id = UlidCreator.getUlid();
        Instant createdAt = Instant.now();
        return new Give(new GiveId(id), item, owner, requester, place, Status.REQUESTED, createdAt, null, null);
    }

    public Give(GiveId id, ItemId item, OwnerId owner, RequesterId requester, PlaceId place,
                Status status, Instant lastUpdated, ItemInQr itemInQr, ItemOutQr itemOutQr) {
        this.id = id;
        this.item = item;
        this.owner = owner;
        this.requester = requester;
        this.place = place;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.itemInQr = itemInQr;
        this.itemOutQr = itemOutQr;
    }

    private GiveId id;
    private ItemId item;
    private OwnerId owner;
    private RequesterId requester;
    private PlaceId place;
    private Status status;
    private Instant lastUpdated;
    private ItemInQr itemInQr;
    private ItemOutQr itemOutQr;

    public void accept() {
        if (this.status != Status.REQUESTED) {
            throw new InvalidGiveStateException(this.status);
        }
        this.status = Status.ACCEPTED;
        createItemInOutQrs();
    }

    private void createItemInOutQrs() {
        this.itemInQr = new ItemInQr(id, owner, item);
        this.itemOutQr = new ItemOutQr(id, requester, item);
    }

    public Optional<ItemInQr> getItemInQr() {
        return Optional.ofNullable(itemInQr);
    }

    public String getItemInQrString() {
        return this.itemInQr == null ? null : this.itemInQr.getCode();
    }

    public Optional<ItemOutQr> getItemOutQr() {
        return Optional.ofNullable(itemOutQr);
    }

    public String getItemOutQrString() {
        return this.itemOutQr == null ? null : this.itemOutQr.getCode();
    }

    public boolean canBeCanceled() {
        return this.status == Status.REQUESTED;
    }

    public boolean isAccepted() {
        return this.status == Status.ACCEPTED;
    }
}
