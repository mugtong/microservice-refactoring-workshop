package app.anne.channel.application.queryservices;

import org.springframework.stereotype.Service;

import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.MemberId;
import app.anne.channel.domain.repository.ChannelRepository;
import app.anne.channel.infrastructure.ChannelRecord;
import app.anne.channel.infrastructure.MemberRecord;
import app.anne.channel.infrastructure.MemberRepository;
import app.anne.channel.infrastructure.MessageRecord;
import app.anne.channel.infrastructure.MessageRepository;

import java.util.List;

@Service
public class ChannelQueryService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public ChannelQueryService(ChannelRepository channelRepository, MessageRepository messageRepository, MemberRepository memberRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    public ChannelRecord findByChannelId(ChannelId channelId) {
        ChannelRecord channel = channelRepository.findOne(channelId);
        channel.setChannelSize(memberRepository.getChannelSize(channelId));
        channel.setLastMessage(messageRepository.getLastMessage(channelId));
        return channel;
    }
    
    public List<ChannelRecord> getAllChannels() {
        return channelRepository.findAll();
    }

    public List<ChannelRecord> getMemberChannels(MemberId memberId) {
        List<String> channels = channelRepository.getMemberChannels(memberId);
        return channelRepository.getChannelsById(channels);
    }

    public List<MemberRecord> getChannelMembers(ChannelId channelId) {
        return memberRepository.getChannelMembers(channelId);
    }

    public List<MessageRecord> getMessages(ChannelId channelId, int size, int page) {
        return messageRepository.loadMessages(channelId, size, page);
    }
}
