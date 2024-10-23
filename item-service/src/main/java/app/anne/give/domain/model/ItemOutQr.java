package app.anne.give.domain.model;

import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.util.Qr;

public class ItemOutQr {

    private final String code;

    public ItemOutQr(GiveId id, RequesterId requester, ItemId item) {
        this.code = Qr.generateQrCode(id, requester, item);
    }

    public ItemOutQr(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
