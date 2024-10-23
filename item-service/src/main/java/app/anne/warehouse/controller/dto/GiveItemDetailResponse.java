package app.anne.warehouse.controller.dto;

import lombok.Data;

@Data
public class GiveItemDetailResponse {
    private String giveId;
    private String giveStatus;
    private String ownerId;
    private String requesterId;
    private String itemId;
    private String itemIsbn;
    private String itemTitle;
    private String itemAuthor;
    private String itemPublisher;
    private String itemImageUrl;
    private String itemStatus;
    private boolean canProceed;
    private String reason;
}
