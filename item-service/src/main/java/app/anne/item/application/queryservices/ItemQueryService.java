package app.anne.item.application.queryservices;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRecord;
import org.springframework.stereotype.Service;

import app.anne.item.infrastructure.ItemRepository;

import java.util.List;

@Service
public class ItemQueryService {

    private final ItemRepository itemRepository;

    public ItemQueryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemRecord findByItemId(OwnerId ownerId, ItemId itemId) {
        ItemRecord item = itemRepository.findOne(itemId);
        if (item == null || !ownerId.getValue().equals(item.getOwner())) {
            return null;
        }
        return item;
    }
    
    public List<ItemRecord> getAllItems(OwnerId ownerId) {
        return itemRepository.findAll(ownerId);
    }
}
