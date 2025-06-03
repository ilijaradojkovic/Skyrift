package bees.io.Berzza.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BetResponse {

    private String betId;
    private Long gameId;
    private String userId;
    private String email;
    private LocalDateTime time;
    private double amount;
    private double multiplier;
    private  double cashOut;
    private double autoCashOutMultiplier;
    private boolean isAutoCashout;
}
