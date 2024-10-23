package app.anne.warehouse.application;

import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.ItemInQr;
import app.anne.give.domain.model.ItemOutQr;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.infrastructure.ItemRepository;
import app.anne.warehouse.controller.dto.GiveItemDetailResponse;
import app.anne.warehouse.domain.GiveItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class WarehouseQueryService {

    private final GiveRepository giveRepository;
    private final ItemRepository itemRepository;

    public WarehouseQueryService(GiveRepository giveRepository,
                                 ItemRepository itemRepository) {
        this.giveRepository = giveRepository;
        this.itemRepository = itemRepository;
    }

    public Optional<GiveItemDetailResponse> getIncomingGiveItemDetail(String qr) {
        return retrieveGiveItem(qr, true)
                .map(giveItem -> createGiveItemDetailResponse(giveItem, true));
    }

    public Optional<GiveItemDetailResponse> getOutgoingGiveItemDetail(String qr) {
        return retrieveGiveItem(qr, false)
                .map(giveItem -> createGiveItemDetailResponse(giveItem, false));
    }

    private Optional<GiveItem> retrieveGiveItem(String qr, boolean isIncoming) {
        Optional<Give> findGiveResult;
        if (isIncoming) {
            findGiveResult = giveRepository.findGiveByItemInQr(new ItemInQr(qr));
        } else {
            findGiveResult = giveRepository.findGiveByItemOutQr(new ItemOutQr(qr));
        }

        if (findGiveResult.isEmpty()) {
            return Optional.empty();
        }

        Give give = findGiveResult.get();
        Optional<OwnedItem> findItemResult = itemRepository.findOwnedItem(give.getOwner(), give.getItem());

        if (findItemResult.isEmpty()) {
            return Optional.empty();
        }

        OwnedItem item = findItemResult.get();
        return Optional.of(new GiveItem(give, item));
    }

    private GiveItemDetailResponse createGiveItemDetailResponse(GiveItem item, boolean isIncoming) {
        GiveItemDetailResponse response = new GiveItemDetailResponse();
        response.setGiveId(item.getGiveId().getStringValue());
        response.setGiveStatus(item.getGiveStatus().name());
        response.setOwnerId(item.getOwnerId().getValue());
        response.setRequesterId(item.getRequesterId().getValue());
        response.setItemId(item.getItemId().getValue());
        response.setItemIsbn(item.getItemIsbn());
        response.setItemTitle(item.getItemTitle());
        response.setItemAuthor(item.getItemAuthor());
        response.setItemPublisher(item.getItemPublisher());
        response.setItemImageUrl(item.getItemImageUrl());
        response.setItemStatus(item.getItemStatus().name());
        response.setCanProceed(isIncoming ? item.validIncoming() : item.validOutgoing());
        return response;
    }
}
