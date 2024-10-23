package app.anne.user.application.commandservices;

import org.springframework.stereotype.Service;

import app.anne.user.controller.dto.CreateUserResource;
import app.anne.user.domain.model.User;
import app.anne.user.domain.model.UserId;
import app.anne.user.infrastructure.UserRecord;
import app.anne.user.infrastructure.UserRepository;


@Service
public class UserCommandService {
    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserRecord createUser(CreateUserResource dto) {
        UserId userId = new UserId(dto.getUserId());
        User user = User.builderForCreateNew(userId)
            .email(dto.getEmail())        
            .name(dto.getName())
            .picture(dto.getPicture())
            .givenName(dto.getGivenName())
            .familyName(dto.getFamilyName())
            .build();

        return userRepository.save(user);
    }
}
