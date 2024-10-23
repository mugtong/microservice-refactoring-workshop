package app.anne.give.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeleteGiveRequest {
    @NotBlank
    private String giveId;
    @NotBlank
    private String ownerId;
    @NotBlank
    private String requesterId;
}
