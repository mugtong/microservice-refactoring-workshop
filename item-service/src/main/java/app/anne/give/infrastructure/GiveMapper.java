package app.anne.give.infrastructure;

import app.anne.give.domain.model.*;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.place.domain.model.PlaceId;

import java.time.Instant;

public class GiveMapper {
    public static Give convertToEntity(GiveRecord record) {
        GiveId giveId = GiveId.createFromStringValue(record.getId());
        ItemId itemId = new ItemId(record.getItemId());
        OwnerId ownerId = new OwnerId(record.getOwnerId());
        RequesterId requesterId = new RequesterId(record.getRequesterId());
        PlaceId placeId = new PlaceId(record.getPlaceId());
        Give.Status status = Give.Status.valueOf(record.getStatus());
        Instant lastUpdated = record.getUpdatedAt();
        ItemInQr itemInQr = record.getItemInQr() == null ? null : new ItemInQr(record.getItemInQr());
        ItemOutQr itemOutQr = record.getItemOutQr() == null ? null : new ItemOutQr(record.getItemOutQr());
        return new Give(giveId, itemId, ownerId, requesterId, placeId, status, lastUpdated, itemInQr, itemOutQr);
    }
}
