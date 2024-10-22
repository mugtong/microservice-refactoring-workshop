package app.anne.give.application;

import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.ItemInQr;
import app.anne.give.domain.model.ItemOutQr;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.give.application.exception.WarehouseErrorCode;
import app.anne.give.application.exception.WarehouseException;
import app.anne.item.application.commandservices.ItemCommandService;
import app.anne.user.domain.model.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class WarehouseCommandService {

    private final GiveRepository giveRepository;
    private final ItemCommandService itemCommandService;

    public WarehouseCommandService(GiveRepository giveRepository,
                                   ItemCommandService itemCommandService) {
        this.giveRepository = giveRepository;
        this.itemCommandService = itemCommandService;
    }

    public void createIncomingItem(ItemInQr itemInQr, String inputIsbn) {
        Give give = retrieveMatchingGive(itemInQr.getCode(), inputIsbn, true)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.NOT_EXISTS));

        give.warehouseItem();
        giveRepository.saveWarehoused(give);

        // TODO send message to requester
    }

    public void createOutgoingItem(ItemOutQr itemOutQr, String inputIsbn) {
        Give give = retrieveMatchingGive(itemOutQr.getCode(), inputIsbn, false)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.NOT_EXISTS));
        give.complete();

        UserId newOwnerId = new UserId(give.getRequester().getValue());
        itemCommandService.transferItem(give.getOwnerId(), give.getItemId(), newOwnerId, give.getPlace());

        giveRepository.saveCompleted(give);

        // TODO send message to requester
    }

    private Optional<Give> retrieveMatchingGive(String qr, String inputIsbn, boolean isIncoming) {
        Optional<Give> findGiveResult;
        if (isIncoming) {
            findGiveResult = giveRepository.findGiveByItemInQr(new ItemInQr(qr));
        } else {
            findGiveResult = giveRepository.findGiveByItemOutQr(new ItemOutQr(qr));
        }

        if (findGiveResult.isEmpty()) {
            log.debug("give not found with qr: '{}' (isIncoming={})", qr, isIncoming);
            return Optional.empty();
        }

        Give give = findGiveResult.get();
        if (give.isIsbnMatched(inputIsbn)) {
            return Optional.of(give);

        } else {
            log.debug("item isbn not matched (input: '{}', found: '{}', '{}')",
                    inputIsbn, give.getItem().getIsbn(), give.getItem().getIsbn13());
            return Optional.empty();
        }
    }
}
