package app.anne.user.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserResource {
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String mobile;
    private String description;
}
