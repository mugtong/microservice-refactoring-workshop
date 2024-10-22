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
    private String ownerId;
    private String requesterId;
    private String placeId;
    private String itemId;
    private String itemIsbn;
    private String itemIsbn13;
    private String itemTitle;
    private String itemAuthor;
    private String itemPublisher;
    private String itemImageUrl;
    private String status;
}
