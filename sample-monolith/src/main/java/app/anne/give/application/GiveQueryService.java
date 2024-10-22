package app.anne.give.application;

import java.util.List;
import java.util.Optional;

import app.anne.give.controller.dto.GiveResponse;
import app.anne.give.controller.transform.CreateGiveCommandDTOAssembler;
import app.anne.give.domain.model.*;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnerId;
import org.springframework.stereotype.Service;

@Service
public class GiveQueryService {
    private final GiveRepository giveRepository;

    public GiveQueryService(GiveRepository giveRepository) {
        this.giveRepository = giveRepository;
    }

    public List<GiveResponse> findByOwner(OwnerId ownerId) {
        return giveRepository.findByOwner(ownerId)
                .stream()
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity)
                .toList();
    }

    public Optional<GiveResponse> findOneByOwner(OwnerId ownerId, GiveId giveId) {
        Give give = giveRepository.findGiveByOwner(ownerId, giveId);
        return Optional.ofNullable(give)
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity);
    }

    public List<GiveResponse> findByRequester(RequesterId requesterId) {
        return giveRepository.findByRequester(requesterId)
                .stream()
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity)
                .toList();
    }

    public Optional<GiveResponse> findOneByRequester(RequesterId requesterId, GiveId giveId) {
        Give give = giveRepository.findGiveByRequester(requesterId, giveId);
        return Optional.ofNullable(give)
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity);
    }

    public Optional<GiveResponse> findByItemInQr(ItemInQr itemInQr) {
        return giveRepository.findGiveByItemInQr(itemInQr)
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity);
    }

    public Optional<GiveResponse> findByItemOutQr(ItemOutQr itemOutQr) {
        return giveRepository.findGiveByItemOutQr(itemOutQr)
                .map(CreateGiveCommandDTOAssembler::toDTOFromEntity);
    }
}
