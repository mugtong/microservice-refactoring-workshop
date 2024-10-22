package app.anne.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.anne.user.application.commandservices.UserCommandService;
import app.anne.user.application.queryservices.UserQueryService;
import app.anne.user.controller.dto.CreateUserResource;
import app.anne.user.controller.dto.UpdateUserResource;
import app.anne.user.controller.dto.UserResponse;
import app.anne.user.controller.dto.UserStatResponse;
import app.anne.user.domain.model.UserId;
import app.anne.user.infrastructure.UserRecord;
import app.anne.user.infrastructure.UserStatRecord;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserResource createUserResource) {
        UserRecord user = userCommandService.createUser(createUserResource);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserRecord user = userQueryService.getUserByUserId(new UserId(userId));
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/stats")
    public ResponseEntity<UserStatResponse> getUserStats(@PathVariable("userId") String userId) {
        UserStatRecord stat = userQueryService.getUserStatByUserId(new UserId(userId));
        return new ResponseEntity<>(new UserStatResponse(stat), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UpdateUserResource updateUserResource) {
        UserRecord user = userCommandService.updateUser(new UserId(userId), updateUserResource);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @DeleteMapping("users/{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userCommandService.deleteUser(new UserId(userId));
    }
}