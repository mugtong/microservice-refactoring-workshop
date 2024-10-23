package app.anne.channel.application.commandservices;

import app.anne.channel.controller.dto.CreateChannelResource;
import app.anne.channel.domain.model.Channel;
import app.anne.channel.domain.model.ChannelId;
import app.anne.channel.domain.model.ChannelType;
import app.anne.channel.domain.repository.ChannelRepository;
import app.anne.channel.infrastructure.ChannelRecord;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;

@Service
public class ChannelCommandService {
    private ChannelRepository channelRepository;

    public ChannelCommandService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public ChannelRecord createChannel(CreateChannelResource dto) {
        Ulid ulid = UlidCreator.getUlid();
        ChannelId channelId = new ChannelId(ulid.toString());
        Channel channel = Channel.builderForCreateNew(channelId)
            .type(ChannelType.valueOf(dto.getType()))
            .name(dto.getName())
            .description(dto.getDescription())
            .imageUrl(dto.getImageUrl())
            .build();

        return channelRepository.save(channel);
    }

    // public Channel updateChannel(String channelId, Channel newChannel) {
    //     Channel oldChannel = channelRepository.findOne(String.format("%s#%s", prefix, channelId), partitionKey);

    //     newChannel.setPk(oldChannel.getPk());
    //     newChannel.setSk(oldChannel.getSk());
    //     // newChannel.setCreatedDate(oldChannel.getCreatedDate());

    //     channelRepository.save(newChannel);

    //     return newChannel;
    // }

    public void deleteChannel(ChannelId channelId) {
        channelRepository.removeItem(channelId);
    }
}
