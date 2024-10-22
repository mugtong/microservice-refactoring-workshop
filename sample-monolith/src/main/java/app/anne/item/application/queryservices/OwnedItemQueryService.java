package app.anne.item.application.queryservices;

import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.infrastructure.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnedItemQueryService {

    private final ItemRepository ownedItemRepository;

    public OwnedItemQueryService(ItemRepository ownedItemRepository) {
        this.ownedItemRepository = ownedItemRepository;
    }

    public Optional<OwnedItem> findOwnedItem(OwnerId ownerId, ItemId itemId) {
        return ownedItemRepository.findOwnedItem(ownerId, itemId);
    }
}
