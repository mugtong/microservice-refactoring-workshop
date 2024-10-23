package app.anne.give.application;

import app.anne.give.application.exception.InvalidDeleteGiveRequestException;
import app.anne.give.controller.dto.CreateGiveRequest;
import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.GiveId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.give.domain.model.RequesterId;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.application.exception.InvalidItemException;
import app.anne.item.application.queryservices.OwnedItemQueryService;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.place.domain.model.PlaceId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GiveCommandService {

    private final OwnedItemQueryService ownedItemQueryService;
    private final GiveRepository giveRepository;

    public GiveCommandService(OwnedItemQueryService ownedItemQueryService,
                              GiveRepository giveRepository) {
        this.ownedItemQueryService = ownedItemQueryService;
        this.giveRepository = giveRepository;
    }

    public GiveId createRequest(CreateGiveRequest request) {
        ItemId itemId = new ItemId(request.getItemId());
        OwnerId ownerId = new OwnerId(request.getOwnerId());
        Optional<OwnedItem> item = ownedItemQueryService.findOwnedItem(ownerId, itemId);
        if (item.isEmpty()) {
            throw new InvalidItemException(itemId, ownerId);
        }

        RequesterId requesterId = new RequesterId(request.getRequesterId()); // TODO validate requester

        PlaceId place = new PlaceId("000"); // TODO place management

        Give give = Give.createNewInstance(itemId, ownerId, requesterId, place);
        return giveRepository.save(give);
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
}
