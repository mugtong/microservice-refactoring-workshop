package app.anne.channel.controller.dto;

import java.time.Instant;

import app.anne.channel.infrastructure.MemberRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String memberId;
    private String name;
    private String imageUrl;
    private Instant updatedAt;

    public MemberResponse(MemberRecord member) {
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.imageUrl = member.getImageUrl();
        this.updatedAt = member.getUpdatedAt();
    }
}
