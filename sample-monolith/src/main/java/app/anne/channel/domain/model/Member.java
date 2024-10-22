package app.anne.channel.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class Member {
    private final ChannelId channel;
    private MemberId member;
    private String name;
    private String imageUrl;
    private Instant updatedAt;

    public static MemberBuilder builderForCreateNew(ChannelId channelId) {
        return hiddenBuilder()
                .channel(channelId)
                .updatedAt(Instant.now());
    }

    // public static MemberBuilder builderForExisting(MemberId memberId) {
    //     return hiddenBuilder()
    //             .member(memberId);
    // }
}
