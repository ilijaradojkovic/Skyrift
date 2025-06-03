package bees.io.Berzza.domain.responses;

import bees.io.Berzza.domain.enums.GameState;

public record GameStateResponse(
        GameState gameState,
        long tick,
        double lastValue,
        Long gameId
) {
}
