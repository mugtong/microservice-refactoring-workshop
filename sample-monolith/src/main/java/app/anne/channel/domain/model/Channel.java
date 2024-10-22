package app.anne.channel.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.List;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class Channel {

    private final ChannelId channel;
    private ChannelType type;
    private String name;
    private String description;
    private String imageUrl;
    private List<Moderator> moderators;
    private Instant updatedAt;

    public static ChannelBuilder builderForCreateNew(ChannelId channelId) {
        return hiddenBuilder()
                .channel(channelId)
                .updatedAt(Instant.now());
    }

    public static ChannelBuilder builderForExisting(ChannelId channelId) {
        return hiddenBuilder()
                .channel(channelId);
    }
}