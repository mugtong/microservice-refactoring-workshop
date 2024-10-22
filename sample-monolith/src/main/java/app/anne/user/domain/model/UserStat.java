package app.anne.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class UserStat {
    private final UserId user;
    private BigDecimal balance;
    private int numItems;
    private int numShares;
    private int numGiftsSent;
    private int numGiftsReceived;

    public static UserStatBuilder builderForCreateNew(UserId userId) {
        return hiddenBuilder()
                .user(userId)
                .balance(new BigDecimal(0))
                .numItems(0)
                .numShares(0)
                .numGiftsSent(0)
                .numGiftsReceived(0);
    }

    public static UserStatBuilder builderForExisting(UserId userId) {
        return hiddenBuilder()
                .user(userId);
    }
}
