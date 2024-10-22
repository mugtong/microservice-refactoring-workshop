package app.anne.channel.controller.dto;

import java.time.Instant;

import app.anne.channel.infrastructure.MessageRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String channelId;
    private String messageId;
    private String body;
    private String sender;
    private Instant updatedAt;

    public MessageResponse(MessageRecord message) {
        this.channelId = message.getPk();
        this.messageId = message.getSk();
        this.body = message.getBody();
        this.sender = message.getSender();
    }
}
