package app.anne.give.infrastructure;

import app.anne.give.domain.model.*;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.place.domain.model.PlaceId;

import java.time.Instant;

public class GiveMapper {
    public static Give convertToEntity(GiveRecord record) {
        if (record == null) {
            return null;
        }

        GiveItem item = GiveItem.builder()
                .id(new ItemId(record.getItemId()))
                .ownerId(new OwnerId(record.getOwnerId()))
                .isbn(record.getItemIsbn())
                .isbn13(record.getItemIsbn13())
                .title(record.getItemTitle())
                .author(record.getItemAuthor())
                .publisher(record.getItemPublisher())
                .imageUrl(record.getItemImageUrl())
                .build();

        GiveId giveId = GiveId.createFromStringValue(record.getId());
        RequesterId requesterId = new RequesterId(record.getRequesterId());
        PlaceId placeId = new PlaceId(record.getPlaceId());
        Give.Status status = Give.Status.valueOf(record.getStatus());
        Instant lastUpdated = record.getUpdatedAt();
        ItemInQr itemInQr = record.getItemInQr() == null ? null : new ItemInQr(record.getItemInQr());
        ItemOutQr itemOutQr = record.getItemOutQr() == null ? null : new ItemOutQr(record.getItemOutQr());
        return new Give(giveId, item, requesterId, placeId, status, lastUpdated, itemInQr, itemOutQr);
    }
}
