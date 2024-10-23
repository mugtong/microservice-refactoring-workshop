package app.anne.give.domain.repository;

import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.model.RequesterId;
import app.anne.give.infrastructure.GiveRecord;
import app.anne.item.domain.model.aggregates.OwnerId;

import java.util.List;

public interface GiveRecordRepository {
    List<GiveRecord> findByOwner(OwnerId ownerId);
    GiveRecord findOneByOwner(OwnerId ownerId, GiveId giveId);
    List<GiveRecord> findByRequester(RequesterId requesterId);
    GiveRecord findOneByRequester(RequesterId requesterId, GiveId giveId);
}
