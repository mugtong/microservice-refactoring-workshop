package app.anne.channel.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMemberResource {
    private String memberId;
    private String name;
    private String imageUrl;
}
