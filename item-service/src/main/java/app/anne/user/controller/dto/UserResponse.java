package app.anne.user.controller.dto;

import java.time.Instant;
import java.util.List;

import app.anne.user.infrastructure.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private List<String> locations;
    private Instant updatedAt;


    public UserResponse(UserRecord user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
        this.locations = user.getLocations();
        this.updatedAt = user.getUpdatedAt();
    }
}
