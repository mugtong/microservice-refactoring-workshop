package app.anne.feed.application.queryservices;

import app.anne.feed.controller.dto.FeedDTO;
import app.anne.feed.controller.dto.FeedListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import app.anne.feed.infrastructure.FeedRepository;
import app.anne.feed.infrastructure.FeedRecord;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeedQueryService {

    private final FeedRepository feedsRepository;

    public FeedQueryService(FeedRepository feedsRepository) {
        this.feedsRepository = feedsRepository;
    }

    public FeedListResponse searchFeeds(String lastRetrievedFeedId) {
        Optional<FeedRecord> lastFeed;
        if (lastRetrievedFeedId == null) {
            lastFeed = Optional.empty();
        } else {
            lastFeed = feedsRepository.retrieveFeedWithId(lastRetrievedFeedId);
            if (lastFeed.isEmpty()) {
                log.info("Feed of id '{}' not found.", lastRetrievedFeedId);
            }
        }

        List<FeedRecord> feedItems = feedsRepository.retrieveFeeds(lastFeed.orElse(null));
        List<FeedDTO> feedItemDtoList = feedItems.stream()
                .map(FeedDTO::new)
                .toList();

        return new FeedListResponse(lastRetrievedFeedId, feedItemDtoList);
    }
}
