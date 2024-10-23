package app.anne.warehouse.domain.repository;

import app.anne.warehouse.domain.GiveItem;

public interface WarehouseRepository {
    void saveReceived(GiveItem giveItem);
}
