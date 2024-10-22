package app.anne.item.application.commandservices;

import app.anne.give.application.exception.WarehouseErrorCode;
import app.anne.give.application.exception.WarehouseException;
import app.anne.item.application.exception.UserNowFoundException;
import app.anne.item.controller.dto.CreateItemResource;
import app.anne.item.domain.model.aggregates.ItemHistory;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRepository;
import app.anne.item.infrastructure.ItemRecord;

import java.util.List;

import app.anne.place.domain.PlaceRepository;
import app.anne.place.domain.model.Place;
import app.anne.place.domain.model.PlaceId;
import app.anne.user.domain.model.UserId;
import app.anne.user.domain.model.User;
import app.anne.user.infrastructure.UserRepository;
import app.anne.user.infrastructure.UserStatRecord;

import org.springframework.stereotype.Service;

@Service
public class ItemCommandService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public ItemCommandService(ItemRepository itemRepository,
                              UserRepository userRepository,
                              PlaceRepository placeRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    public ItemRecord createItem(CreateItemResource dto) {
        OwnerId ownerId = new OwnerId(dto.getOwnerId());
        List<ItemRecord> items = itemRepository.findByOwnerIdAndIsbn13(ownerId, dto.getIsbn13());
        if (!items.isEmpty()) {
            return items.get(0);
        }

        OwnedItem ownedItem = OwnedItem.builderForCreateNew(ownerId)
                .ownerName(dto.getOwnerName())
                .ownerImageUrl(dto.getOwnerImageUrl())
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
                .build();

        final UserId ownerUserId = new UserId(dto.getOwnerId());
        User owner = userRepository.findOneById(ownerUserId)
                .orElseThrow(() -> new UserNowFoundException(ownerUserId));
        ItemHistory history = ItemHistory.createItemCreatedHistory(ownedItem.getItem(), owner);
        UserStatRecord userStat = userRepository.findStatByUserId(ownerUserId);

        return itemRepository.saveNew(ownedItem, history, userStat);
    }

    public void deleteItem(OwnerId ownerId, ItemId itemId) {
        // TODO: check if owner matches with the item
        itemRepository.removeItem(itemId);
    }

    public void transferItem(OwnerId currentOwnerId, ItemId itemId, UserId newOwnerId, PlaceId placeId) {
        User newOwner = userRepository.findOneById(newOwnerId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVALID_USER));
        OwnedItem item = itemRepository.findOwnedItem(currentOwnerId, itemId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVALID_ITEM));
        item.registerTo(newOwner);

        Place place = placeRepository.findById(placeId)
                .orElse(Place.unknown(placeId));

        ItemHistory itemHistory = ItemHistory.createItemTransferHistory(item.getItem(), newOwner, place);
        UserStatRecord currentOwnerStat = userRepository.findStatByUserId(new UserId(currentOwnerId.getValue()));
        UserStatRecord newOwnerStat = userRepository.findStatByUserId(newOwnerId);        
        itemRepository.saveTransferred(item, itemHistory, currentOwnerStat, newOwnerStat);
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
