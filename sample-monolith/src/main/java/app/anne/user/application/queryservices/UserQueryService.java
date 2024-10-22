package app.anne.user.application.queryservices;

import org.springframework.stereotype.Service;

import app.anne.user.domain.model.UserId;
import app.anne.user.infrastructure.UserRecord;
import app.anne.user.infrastructure.UserRepository;
import app.anne.user.infrastructure.UserStatRecord;

@Service
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserRecord getUserByUserId(UserId userId) {
        return userRepository.findByUserId(userId);
    }

    public UserStatRecord getUserStatByUserId(UserId userId) {
        return userRepository.findStatByUserId(userId);
    }
}
