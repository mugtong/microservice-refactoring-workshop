package app.anne.channel.controller.dto;

import java.time.Instant;
import java.util.List;

import app.anne.channel.domain.model.Moderator;
import app.anne.channel.infrastructure.ChannelRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResponse {
    private String channelId;
    private String name;
    private String description;
    private String imageUrl;
    private List<Moderator> moderators;
    private Instant updatedAt;

    public ChannelResponse(ChannelRecord channel) {
        this.channelId = channel.getChannelId();
        this.name = channel.getName();
        this.description = channel.getDescription();
        this.imageUrl = channel.getImageUrl();
        this.moderators = channel.getModerators();
        this.updatedAt = channel.getUpdatedAt();
    }
}
