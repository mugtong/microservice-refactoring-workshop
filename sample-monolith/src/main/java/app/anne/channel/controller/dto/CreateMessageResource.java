package app.anne.channel.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMessageResource {
    private String body;
    private String sender;
}
