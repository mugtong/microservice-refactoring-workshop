package app.anne.channel.domain.repository;

import app.anne.channel.domain.model.Channel;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.MemberId;
import app.anne.channel.infrastructure.ChannelRecord;

import java.util.List;

public interface ChannelRepository {
    ChannelRecord findOne(ChannelId channelId);
    List<ChannelRecord> findAll();
    List<String> getMemberChannels(MemberId memberId);
    ChannelRecord createChannel(Channel channel);
    void removeItem(ChannelId channelId);
    List<ChannelRecord> getChannelsById(List<String> channdlIds);
}

