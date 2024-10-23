package app.anne.item.application.commandservices;

import app.anne.item.controller.dto.CreateItemResource;
import app.anne.item.controller.dto.ItemResponse;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItemStatus;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRepository;
import app.anne.item.infrastructure.ItemRecord;
import org.springframework.stereotype.Service;

@Service
public class ItemCommandService {
    private final ItemRepository itemRepository;

    public ItemCommandService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemRecord createItem(CreateItemResource dto) {
        OwnerId ownerId = new OwnerId(dto.getOwnerId());
        OwnedItem ownedItem = OwnedItem.builderForCreateNew(ownerId)
                .adult(dto.getAdult())
                .author(dto.getAuthor())
                .categoryId(dto.getCategoryId())
                .categoryName(dto.getCategoryName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .isbn(dto.getIsbn())
                .isbn13(dto.getIsbn13())
                .link(dto.getLink())
                .pubDate(dto.getPubDate())
                .publisher(dto.getPublisher())
                .subTitle(dto.getSubTitle())
                .title(dto.getTitle())
                .type(dto.getType())
                .build();

        return itemRepository.save(ownedItem);
    }

    public void deleteItem(OwnerId ownerId, ItemId itemId) {
        // TODO: check if owner matches with the item
        itemRepository.removeItem(itemId);
    }

    public ItemResponse updateItemStatus(String itemId, Enum<OwnedItemStatus> status) {
        ItemRecord itemRecord = itemRepository.findOne(new ItemId(itemId));
        itemRecord.setStatus(status.name());
        ItemRecord updatedItemRecord = itemRepository.updateItem(itemRecord);
        return new ItemResponse(updatedItemRecord);
    }

    /*
    public ItemRecord updateItem(String itemId, ItemRecord newItem) {
        ItemRecord oldItem = itemRepository.findOne(String.format("%s#%s", prefix, itemId), partitionKey);

        newItem.setPk(oldItem.getPk());
        newItem.setSk(oldItem.getSk());
        // newItem.setCreatedDate(oldItem.getCreatedDate());

        newItem.setAdult(getNewValue(oldItem.getAdult(), newItem.getAdult()));
        newItem.setAuthor(getNewValue(oldItem.getAuthor(), newItem.getAuthor()));
        newItem.setCategoryId(getNewValue(oldItem.getCategoryId(), newItem.getCategoryId()));
        newItem.setCategoryName(getNewValue(oldItem.getCategoryName(), newItem.getCategoryName()));
        newItem.setDescription(getNewValue(oldItem.getDescription(), newItem.getDescription()));
        newItem.setImageUrl(getNewValue(oldItem.getImageUrl(), newItem.getImageUrl()));
        newItem.setIsbn(getNewValue(oldItem.getIsbn(), newItem.getIsbn()));
        newItem.setIsbn13(getNewValue(oldItem.getIsbn13(), newItem.getIsbn13()));
        newItem.setItemId(getNewValue(oldItem.getItemId(), newItem.getItemId()));
        newItem.setLink(getNewValue(oldItem.getLink(), newItem.getLink()));
        newItem.setPubDate(getNewValue(oldItem.getPubDate(), newItem.getPubDate()));
        newItem.setPublisher(getNewValue(oldItem.getPublisher(), newItem.getPublisher()));
        newItem.setSubTitle(getNewValue(oldItem.getSubTitle(), newItem.getSubTitle()));
        newItem.setSubTitle(getNewValue(oldItem.getSubTitle(), newItem.getSubTitle()));
        newItem.setTitle(getNewValue(oldItem.getTitle(), newItem.getTitle()));
        newItem.setType(getNewValue(oldItem.getType(), newItem.getType()));


        itemRepository.save(newItem);

        return newItem;
    }

    private <T> T getNewValue(T oldValue, T newValue) {
        return newValue != null ? newValue : oldValue;
    }
    */
}
