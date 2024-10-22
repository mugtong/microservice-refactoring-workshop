package app.anne.give.application;

import app.anne.give.application.exception.GiveNotFoundException;
import app.anne.give.application.exception.InvalidDeleteGiveRequestException;
import app.anne.give.controller.dto.CreateGiveRequest;
import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.GiveId;
import app.anne.give.domain.model.GiveItem;
import app.anne.item.domain.model.aggregates.OwnedItemStatus;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.give.domain.model.RequesterId;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.application.exception.InvalidItemException;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.infrastructure.ItemRepository;
import app.anne.place.domain.model.PlaceId;
import app.anne.util.SqsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GiveCommandService {

    private final ItemRepository itemRepository;
    private final GiveRepository giveRepository;

    public GiveCommandService(ItemRepository itemRepository,
                              GiveRepository giveRepository) {
        this.itemRepository = itemRepository;
        this.giveRepository = giveRepository;
    }

    public GiveId createRequest(CreateGiveRequest request) {
        ItemId itemId = new ItemId(request.getItemId());
        OwnerId ownerId = new OwnerId(request.getOwnerId());
        Optional<OwnedItem> itemOpt = itemRepository.findOwnedItem(ownerId, itemId);
        if (itemOpt.isEmpty()) {
            throw new InvalidItemException(itemId, ownerId);
        }

        RequesterId requesterId = new RequesterId(request.getRequesterId()); // TODO validate requester

        PlaceId place = new PlaceId("000"); // TODO place management

        OwnedItem item = itemOpt.get();
        GiveItem giveItem = GiveItem.builder()
                .id(item.getItem())
                .ownerId(item.getOwner())
                .isbn(item.getIsbn())
                .isbn13(item.getIsbn13())
                .title(item.getTitle())
                .author(item.getAuthor())
                .publisher(item.getPublisher())
                .imageUrl(item.getImageUrl())
                .build();
        Give give = Give.createNewInstance(giveItem, requesterId, place);
        return giveRepository.saveNew(give);
    }

    public void deleteRequest(GiveId giveId, OwnerId ownerId, RequesterId requesterId) {
        Give give = giveRepository.findGiveByOwner(ownerId, giveId);
        if (give == null) {
            throw new InvalidDeleteGiveRequestException("owner-give record not found");
        }
        if (!requesterId.equals(give.getRequester())) {
            throw new InvalidDeleteGiveRequestException("requester not matched");
        }
        if (!give.canBeCanceled()) {
            throw new InvalidDeleteGiveRequestException("give already accepted or completed");
        }
        giveRepository.delete(give);
    }

    public void acceptRequest(final GiveId giveId, final OwnerId ownerId) {
        final Give give = giveRepository.findGiveByOwner(ownerId, giveId);
        if (give == null) {
            throw new GiveNotFoundException(giveId, ownerId);
        }
        give.accept();

        SqsService.sendMessage(give.getItemId().getValue());

        giveRepository.saveAccepted(give);
    }
}
