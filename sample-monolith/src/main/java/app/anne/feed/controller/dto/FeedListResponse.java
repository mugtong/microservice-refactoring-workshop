package app.anne.feed.controller.dto;

import java.util.List;

import lombok.Data;

@Data
public class FeedListResponse {
    private List<FeedDTO> items;
    private String lastFeedId;

    public FeedListResponse(String previousLastFeedId, List<FeedDTO> items) {
        this.items = items;
        if (items.isEmpty()) {
            this.lastFeedId = previousLastFeedId;
        } else {
            this.lastFeedId = items.get(items.size() - 1).getFeedId();
        }
    }
}
