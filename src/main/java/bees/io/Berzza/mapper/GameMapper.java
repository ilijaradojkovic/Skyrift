package bees.io.Berzza.mapper;

import bees.io.Berzza.domain.Game;
import bees.io.Berzza.domain.responses.GameHistoryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameMapper {

    public GameHistoryResponse toGameHistoryResponse(Game game) {
        return new GameHistoryResponse(game.getId(), game.getMaxMultiplier());
    }

    public List<GameHistoryResponse> toGameHistoryResponses(List<Game> topNByOrderByCreatedAtDesc) {
        List<GameHistoryResponse> gameHistoryResponses = new ArrayList<>();
        for (Game game : topNByOrderByCreatedAtDesc) {
            gameHistoryResponses.add(toGameHistoryResponse(game));
        }
        return gameHistoryResponses;
    }
}
