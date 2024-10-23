package app.anne.warehouse.application;

import app.anne.give.domain.model.Give;
import app.anne.give.domain.model.ItemInQr;
import app.anne.give.domain.repository.GiveRepository;
import app.anne.item.domain.model.aggregates.OwnedItem;
import app.anne.item.infrastructure.ItemRepository;
import app.anne.warehouse.domain.GiveItem;
import app.anne.warehouse.domain.repository.WarehouseRepository;
import app.anne.warehouse.exception.WarehouseErrorCode;
import app.anne.warehouse.exception.WarehouseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class WarehouseCommandService {

    private final GiveRepository giveRepository;
    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;

    public WarehouseCommandService(GiveRepository giveRepository,
                                 ItemRepository itemRepository,
                                 WarehouseRepository warehouseRepository) {
        this.giveRepository = giveRepository;
        this.itemRepository = itemRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public void createIncomingGiveItem(String qr) {
        Optional<Give> findGiveResult = giveRepository.findGiveByItemInQr(new ItemInQr(qr));
        if (findGiveResult.isEmpty()) {
            log.debug("give not found with qr: '{}'", qr);
            throw new WarehouseException(WarehouseErrorCode.NOT_EXISTS);
        }
        Give give = findGiveResult.get();

        Optional<OwnedItem> findItemResult = itemRepository.findOwnedItem(give.getOwner(), give.getItem());
        if (findItemResult.isEmpty()) {
            log.debug("item not found with owner: '{}' and item: '{}'",
                    give.getOwner().getValue(), give.getItem().getValue());
            throw new WarehouseException(WarehouseErrorCode.NOT_EXISTS);
        }

        OwnedItem item = findItemResult.get();
        GiveItem giveItem = new GiveItem(give, item);
        giveItem.receive();

        warehouseRepository.saveReceived(giveItem);

        // TODO send message to requester
    }
}
