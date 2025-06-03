package bees.io.Berzza.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO {
    private String betId;
    private Long gameId;
    private String userId;
    private LocalDateTime time;
    private double amount;
    private double multiplier;
    private  double cashOut;
    private double autoCashOutMultiplier;
    private boolean isAutoCashout;

}
