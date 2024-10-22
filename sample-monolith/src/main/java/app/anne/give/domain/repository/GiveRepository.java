package app.anne.give.domain.repository;

import app.anne.give.domain.model.*;
import app.anne.item.domain.model.aggregates.OwnerId;

import java.util.List;
import java.util.Optional;

public interface GiveRepository {
    List<Give> findByOwner(OwnerId ownerId);
    Give findGiveByOwner(OwnerId ownerId, GiveId giveId);
    List<Give> findByRequester(RequesterId requesterId);
    Give findGiveByRequester(RequesterId requesterId, GiveId giveId);
    Optional<Give> findGiveByItemInQr(ItemInQr itemInQr);
    Optional<Give> findGiveByItemOutQr(ItemOutQr itemOutQr);
    GiveId saveNew(Give give);
    void delete(Give give);
    void saveAccepted(Give give);
    void saveWarehoused(Give give);
    void saveCompleted(Give give);
}
