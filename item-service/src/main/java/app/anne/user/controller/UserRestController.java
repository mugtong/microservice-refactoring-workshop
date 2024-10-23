package app.anne.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.anne.user.application.commandservices.UserCommandService;
// import app.anne.user.application.queryservices.UserQueryService;
import app.anne.user.controller.dto.CreateUserResource;
import app.anne.user.controller.dto.UserResponse;
import app.anne.user.infrastructure.UserRecord;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
public class UserRestController {

    private final UserCommandService userCommandService;

    public UserRestController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserResource createUserResource) {
        UserRecord user = userCommandService.createUser(createUserResource);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    // @DeleteMapping("users/{userId}")
    // public void deleteUser(@PathVariable("userId") String userId) {
    //     userCommandService.deleteUser(new UserId(userId));
    // }
}