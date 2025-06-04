package bees.io.Berzza.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Bet {

    @Id
    private String id;


    //Euro
    private double amountBaseCurrency;

    private double multiplier;



    private double cashOutBaseCurrency;



    private double profitBaseCurrency;

    private boolean isAutoCashout;
    private double autoCashOutMultiplier;
    private boolean alreadyCashedOut;

    private String username;

    private Long gameId;



    private LocalDateTime timestamp;

}
