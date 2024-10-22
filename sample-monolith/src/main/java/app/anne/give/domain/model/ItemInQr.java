package app.anne.give.domain.model;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.util.Qr;

public class ItemInQr {

    private final String code;

    public ItemInQr(GiveId id, OwnerId owner, ItemId item) {
        this.code = Qr.generateQrCode(id, owner, item);
    }

    public ItemInQr(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
