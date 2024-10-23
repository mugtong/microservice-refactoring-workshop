package app.anne.channel.application.queryservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.repository.ChannelRepository;
import app.anne.channel.infrastructure.ChannelRecord;

import java.util.List;

@Service
public class ChannelQueryService {

    private final ChannelRepository channelsRepository;

    public ChannelQueryService(ChannelRepository channelsRepository) {
        this.channelsRepository = channelsRepository;
    }

    public ChannelRecord findByChannelId(ChannelId channelId) {
        return channelsRepository.findOne(channelId);
    }
    
    public List<ChannelRecord> getAllChannels() {
        return channelsRepository.findAll();
    }
}
