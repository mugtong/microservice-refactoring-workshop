package app.anne.give.application;

import java.util.List;
import java.util.Optional;

import app.anne.give.controller.dto.GiveResponse;
import app.anne.give.controller.transform.CreateGiveCommandDTOAssembler;
import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.model.RequesterId;
import app.anne.give.domain.repository.GiveRecordRepository;
import app.anne.item.domain.model.aggregates.OwnerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.anne.give.infrastructure.GiveRecord;

@Service
public class GiveQueryService {
    @Autowired
    private final GiveRecordRepository giveRepository;

    public GiveQueryService(GiveRecordRepository giveRepository) {
        this.giveRepository = giveRepository;
    }

    public List<GiveResponse> findByOwner(OwnerId ownerId) {
        return giveRepository.findByOwner(ownerId)
                .stream()
                .map(CreateGiveCommandDTOAssembler::toDTOFromCommand)
                .toList();
    }

    public Optional<GiveResponse> findOneByOwner(OwnerId ownerId, GiveId giveId) {
        GiveRecord giveRecord = giveRepository.findOneByOwner(ownerId, giveId);
        return Optional.ofNullable(giveRecord)
                .map(CreateGiveCommandDTOAssembler::toDTOFromCommand);
    }

    public List<GiveResponse> findByRequester(RequesterId requesterId) {
        return giveRepository.findByRequester(requesterId)
                .stream()
                .map(CreateGiveCommandDTOAssembler::toDTOFromCommand)
                .toList();
    }

    public Optional<GiveResponse> findOneByRequester(RequesterId requesterId, GiveId giveId) {
        GiveRecord giveRecord = giveRepository.findOneByRequester(requesterId, giveId);
        return Optional.ofNullable(giveRecord)
                .map(CreateGiveCommandDTOAssembler::toDTOFromCommand);
    }
}
