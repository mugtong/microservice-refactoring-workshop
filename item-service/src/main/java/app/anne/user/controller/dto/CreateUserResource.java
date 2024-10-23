package app.anne.user.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserResource {
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
}
