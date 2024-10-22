package app.anne.channel.controller;

import lombok.RequiredArgsConstructor;
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
import app.anne.channel.domain.model.MemberId;
import app.anne.channel.infrastructure.ChannelRecord;
import app.anne.channel.infrastructure.MemberRecord;
import app.anne.channel.infrastructure.MessageRecord;
import app.anne.channel.controller.dto.CreateChannelResource;
import app.anne.channel.controller.dto.CreateMemberResource;
import app.anne.channel.controller.dto.CreateMessageResource;
import app.anne.channel.controller.dto.MemberResponse;
import app.anne.channel.controller.dto.MessageResponse;
import jakarta.validation.Valid;

import java.util.Comparator;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelRestController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRestController.class);

    private final ChannelQueryService channelQueryService;
    private final ChannelCommandService channelCommandService;

    @PostMapping("/channels")
    public ResponseEntity<ChannelResponse> createChannel(@RequestBody CreateChannelResource createChannelResource) {
        ChannelRecord channel = channelCommandService.createChannel(createChannelResource);
        return new ResponseEntity<>(new ChannelResponse(channel), HttpStatus.OK);
    }

    @GetMapping(value = "/channels/{channelId}")
    public ResponseEntity<ChannelResponse> getChannel(@PathVariable("channelId") String channelId) {
        ChannelRecord channel = channelQueryService.findByChannelId(new ChannelId(channelId));
        return new ResponseEntity<>(new ChannelResponse(channel), HttpStatus.OK);
    }

    /*
     * 
     * PK: CHANNEL#<ID>
     * SK: MEMBER#<ID>
     * name: user's name
     * imageUrl: user's profile image
     */
    @PostMapping(value = "/channels/{channelId}/members")
    public ResponseEntity<MemberResponse> joinChannel(@Valid @PathVariable("channelId") String channelId, @RequestBody CreateMemberResource createMemberResource) {
        MemberRecord member = channelCommandService.joinChannel(new ChannelId(channelId), createMemberResource);
        return new ResponseEntity<>(new MemberResponse(member), HttpStatus.OK);
    }

    @GetMapping(value = "/channels/{channelId}/members")
    public List<MemberResponse> getChannelMembers(@PathVariable("channelId") String channelId) {
        return channelQueryService.getChannelMembers(new ChannelId(channelId))
                .stream()
                .map(member -> new MemberResponse(member))
                .sorted(Comparator.comparing(MemberResponse::getUpdatedAt).reversed())
                .toList();
    }

    @GetMapping(value = "/members/{memberId}/channels")
    public List<ChannelResponse> getMemberChannels(@PathVariable("memberId") String memberId) {
        return channelQueryService.getMemberChannels(new MemberId(memberId))
                .stream()
                .map(channel -> new ChannelResponse(channel))
                .sorted(Comparator.comparing(ChannelResponse::getUpdatedAt).reversed())
                .toList();
    }

    // @PostMapping(value = "/channels/{channelId}/messages")
    // public ResponseEntity<MessageResponse> sendMessage(@Valid @PathVariable("channelId") String channelId, @RequestBody CreateMessageResource createMessageResource) {
    //     MessageRecord message = channelCommandService.createMessage(new ChannelId(channelId), createMessageResource);
    //     return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    // }

    @GetMapping(value = "/channels/{channelId}/messages")
    public List<MessageResponse> getChannelMessages(@PathVariable("channelId") String channelId, @RequestParam(required = true) int size, @RequestParam(required = true) int page) {
        return channelQueryService.getMessages(new ChannelId(channelId), size, page)
                .stream()
                .map(message -> new MessageResponse(message))
                .toList();
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