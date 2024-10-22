package app.anne.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class User {
    private final UserId user;
    private UserStatus status;
    private String email;
    private Boolean emailVerified;
    private String phone;
    private Boolean phoneVerified;
    private String name;
    private String picture;
    private String mobile;
    private String description;
    private String locale;
    private String gender;
    private List<String> locations;
    private Instant updatedAt;

    public static UserBuilder builderForCreateNew(UserId userId) {
        return hiddenBuilder()
                .user(userId)
                .status(UserStatus.ACTIVE)
                .locations(new ArrayList<>())
                .updatedAt(Instant.now());
    }

    public static UserBuilder builderForExisting(UserId userId) {
        return hiddenBuilder()
                .user(userId);
    }
}
