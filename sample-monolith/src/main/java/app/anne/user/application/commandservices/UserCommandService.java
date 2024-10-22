package app.anne.user.application.commandservices;

import org.springframework.stereotype.Service;

import app.anne.user.controller.dto.CreateUserResource;
import app.anne.user.controller.dto.UpdateUserResource;
import app.anne.user.domain.model.User;
import app.anne.user.domain.model.UserId;
import app.anne.user.infrastructure.UserRecord;
import app.anne.user.infrastructure.UserRepository;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Base64;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

@Service
public class UserCommandService {
    private final UserRepository userRepository;
    private final S3Client s3Client;

    public UserCommandService(UserRepository userRepository, S3Client s3Client) {
        this.userRepository = userRepository;
        this.s3Client = s3Client;
    }
    
    public UserRecord createUser(CreateUserResource dto) {
        UserId userId = new UserId(dto.getUserId());
        User user = User.builderForCreateNew(userId)
            .email(dto.getEmail())        
            .name(dto.getName())
            .picture(dto.getPicture())
            .build();

        return userRepository.saveNew(user);
    }

    public UserRecord updateUser(UserId userId, UpdateUserResource dto) {
        UserRecord userRecord = userRepository.findByUserId(userId);
        
        if (dto.getName() != null) userRecord.setName(dto.getName());
        if (dto.getEmail() != null) userRecord.setEmail(dto.getEmail());
        if (dto.getMobile() != null) userRecord.setMobile(dto.getMobile());
        if (dto.getDescription() != null) userRecord.setDescription(dto.getDescription());
        if (dto.getPicture() != null) {
            // conver base64 encoded string to file
            // save file to s3
            // rewrite picture to cdn location
            String picture = dto.getPicture();
            byte[] decodedFile = Base64.getDecoder().decode(picture.substring(picture.indexOf(",")+1).getBytes());
            System.out.println(picture.substring(0, picture.indexOf(",")));
            InputStream inputStream = new ByteArrayInputStream(decodedFile);

            // upload picture to s3
            try {
                String profileImage = String.format("images/%s_profile.jpg", dto.getUserId());
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("static.anne.app")
                    .key(profileImage)
                    .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, decodedFile.length));                
                userRecord.setPicture(String.format("https://cdn.anne.app/%s", profileImage));
            } catch (S3Exception se) {
                System.err.println("Service exception thrown.");
                System.err.println(se.awsErrorDetails().errorMessage());
            } catch (SdkClientException ce) {
                System.err.println("Client exception thrown.");
                System.err.println(ce.getMessage());
            }
        }

        return userRepository.updateUser(userRecord);
    }

    public void deleteUser(UserId userId) {
        userRepository.deleteUser(userId);
    }
}
