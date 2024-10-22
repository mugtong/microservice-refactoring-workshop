package app.anne.user.controller.dto;

import app.anne.user.infrastructure.UserStatRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatResponse {
    private BigDecimal balance;
    private int numItems;
    private int numShares;
    private int numGiftsSent;
    private int numGiftsReceived;

    public UserStatResponse(UserStatRecord stat) {
        this.balance = stat.getBalance();
        this.numItems = stat.getNumItems();
        this.numShares = stat.getNumShares();
        this.numGiftsSent = stat.getNumGiftsSent();
        this.numGiftsReceived = stat.getNumGiftsReceived();
    }
}
