package app.anne.feed.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import app.anne.feed.application.queryservices.FeedQueryService;
import app.anne.feed.controller.dto.FeedListResponse;

@CrossOrigin
@Slf4j
@RestController
public class FeedRestController {

    private final FeedQueryService feedQueryService;

    public FeedRestController(FeedQueryService feedQueryService) {
        this.feedQueryService = feedQueryService;
    }

    @GetMapping(value = "/feeds")
    public FeedListResponse searchFeeds(@RequestParam(name = "lastFeedId", required = false) String lastRetrievedFeedId) {
        return feedQueryService.searchFeeds(lastRetrievedFeedId);
    }
}