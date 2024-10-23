package app.anne.channel.domain.repository;

import app.anne.channel.domain.model.Channel;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.infrastructure.ChannelRecord;

import java.util.List;

public interface ChannelRepository {
    ChannelRecord findOne(ChannelId channelId);
    List<ChannelRecord> findAll();
    ChannelRecord save(Channel channel);
    void removeItem(ChannelId channelId);
}

