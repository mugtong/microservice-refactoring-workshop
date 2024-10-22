package app.anne.channel.controller.dto;

import app.anne.channel.domain.model.ChannelType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateChannelResource {
    private String isbn13;
    private ChannelType type;
    private String name;
    private String description;
    private String imageUrl;
}
