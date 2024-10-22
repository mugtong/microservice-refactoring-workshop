package app.anne.channel.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class Message {
    private final ChannelId channel;
    private final MessageId message;
    private String body;
    private String sender;
    private Instant updatedAt;

    public static MessageBuilder builderForCreateNew(ChannelId channelId, MessageId messageId) {
        return hiddenBuilder()
                .channel(channelId)
                .message(messageId)
                .updatedAt(Instant.now());
    }

    public static MessageBuilder builderForExisting(ChannelId channelId, MessageId messageId) {
        return hiddenBuilder()
                .channel(channelId)
                .message(messageId);
    }
}
