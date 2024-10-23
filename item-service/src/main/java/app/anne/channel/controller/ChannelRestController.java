package app.anne.channel.controller;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.anne.channel.application.commandservices.ChannelCommandService;
import app.anne.channel.application.queryservices.ChannelQueryService;
import app.anne.channel.controller.dto.ChannelResponse;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.infrastructure.ChannelRecord;
import app.anne.channel.controller.dto.CreateChannelResource;
import jakarta.validation.Valid;

import java.util.Comparator;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
public class ChannelRestController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRestController.class);

    private ChannelQueryService channelQueryService;
    private ChannelCommandService channelCommandService;

    public ChannelRestController(ChannelQueryService channelQueryService, ChannelCommandService channelCommandService) {
        this.channelQueryService = channelQueryService;
        this.channelCommandService = channelCommandService;
    }

    @PostMapping("/channels")
    public ResponseEntity<ChannelResponse> createChannel(@Valid @RequestBody CreateChannelResource createChannelResource) {
        ChannelRecord channel = channelCommandService.createChannel(createChannelResource);
        return new ResponseEntity<>(new ChannelResponse(channel), HttpStatus.OK);
    }

    @GetMapping(value = "/channels/{channelId}")
    public ResponseEntity<ChannelResponse> getChannel(@PathVariable("channelId") String channelId) {
        ChannelRecord channel = channelQueryService.findByChannelId(new ChannelId(channelId));
        return new ResponseEntity<>(new ChannelResponse(channel), HttpStatus.OK);
    }

    @GetMapping(value = "/channels")
    public List<ChannelResponse> getAllChannels() {
        return channelQueryService.getAllChannels()
                .stream()
                .map(channel -> new ChannelResponse(channel))
                .sorted(Comparator.comparing(ChannelResponse::getUpdatedAt).reversed())
                .toList();
    }
}