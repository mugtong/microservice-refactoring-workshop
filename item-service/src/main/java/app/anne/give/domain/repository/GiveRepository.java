package app.anne.give.domain.repository;

import app.anne.give.domain.model.*;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;

import java.util.Optional;

public interface GiveRepository {
    GiveId save(Give give);
    Give findGiveByOwner(OwnerId ownerId, GiveId giveId);
    Give findGiveByRequester(RequesterId requesterId, GiveId giveId);
    Optional<Give> findGiveByItemInQr(ItemInQr itemInQr);
    Optional<Give> findGiveByItemOutQr(ItemOutQr itemOutQr);
    void delete(Give give);
    void saveAcceptedGiveAndItem(Give give, OwnedItem item);
}
