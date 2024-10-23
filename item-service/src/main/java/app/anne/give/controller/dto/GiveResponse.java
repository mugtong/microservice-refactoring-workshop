package app.anne.give.controller.dto;

import app.anne.give.infrastructure.GiveRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GiveResponse {
    private String giveId;
    private String itemId;
    private String ownerId;
    private String placeId;
    private String requesterId;
    private String status;

    public GiveResponse(GiveRecord giveRecord) {
        this.giveId = giveRecord.getId();
        this.itemId = giveRecord.getItemId();
        this.ownerId = giveRecord.getOwnerId();
        this.placeId = giveRecord.getPlaceId();
        this.requesterId = giveRecord.getRequesterId();
        this.status = giveRecord.getStatus();
    }
}
