package app.anne.give.application;

import app.anne.give.domain.model.*;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnerId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemQrService {
    private final GiveRepository giveRepository;

    public ItemQrService(GiveRepository giveRepository) {
        this.giveRepository = giveRepository;
    }

    public Optional<String> getItemInQrForOwner(OwnerId ownerId, GiveId giveId) {
        Give give = giveRepository.findGiveByOwner(ownerId, giveId);
        return give.getItemInQr()
                .map(ItemInQr::getCode);
    }

    public Optional<String> getItemOutQrForRequester(RequesterId requesterId, GiveId giveId) {
        Give give = giveRepository.findGiveByRequester(requesterId, giveId);
        return give.getItemOutQr()
                .map(ItemOutQr::getCode);
    }
}
