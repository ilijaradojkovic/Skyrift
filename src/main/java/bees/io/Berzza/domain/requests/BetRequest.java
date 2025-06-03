package bees.io.Berzza.domain.requests;


import bees.io.Berzza.domain.enums.BetType;

public record BetRequest(
        double amount,
        String betId,
        double multiplier,
        String username,
        BetType betType,

        boolean isAutoCashout,
        double autoCashOutMultiplier

) {
}
