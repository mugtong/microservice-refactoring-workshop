package app.anne.give.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateGiveRequest {
    @NotBlank
    private String itemId;
    @NotBlank
    private String ownerId;
    @NotBlank
    private String requesterId;
    private String placeId;
}
