package app.anne.channel.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateChannelResource {
    private String type;
    private String name;
    private String description;
    private String imageUrl;
}
