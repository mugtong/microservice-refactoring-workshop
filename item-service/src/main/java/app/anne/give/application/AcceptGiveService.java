package app.anne.give.application;

import app.anne.give.application.exception.GiveNotFoundException;
import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.application.exception.InvalidItemException;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRepository;
import org.springframework.stereotype.Service;


@Service
public class AcceptGiveService {

    private final GiveRepository giveRepository;
    private final ItemRepository itemRepository;

    public AcceptGiveService(GiveRepository giveRepository, ItemRepository itemRepository) {
        this.giveRepository = giveRepository;
        this.itemRepository = itemRepository;
    }

    public void acceptGiveRequest(final GiveId giveId, final OwnerId ownerId) {
        final Give give = giveRepository.findGiveByOwner(ownerId, giveId);
        if (give == null) {
            throw new GiveNotFoundException(giveId, ownerId);
        }
        give.accept();

        final OwnedItem item = itemRepository.findOwnedItem(ownerId, give.getItem())
                .orElseThrow(() -> new InvalidItemException(give.getItem(), ownerId));
        item.pendingReceive();

        giveRepository.saveAcceptedGiveAndItem(give, item);
    }
}
