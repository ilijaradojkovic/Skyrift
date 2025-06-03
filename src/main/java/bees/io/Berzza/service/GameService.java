package bees.io.Berzza.service;

import bees.io.Berzza.domain.Point;
import bees.io.Berzza.domain.enums.GameState;
import bees.io.Berzza.domain.responses.GameHistoryResponse;
import org.springframework.data.redis.core.ZSetOperations;
import reactor.core.publisher.Flux;

import java.util.List;

public interface GameService {


    void generateGames();


    List<GameHistoryResponse> getGameHistory(Integer size);

    GameState getGameState();

    Flux<ZSetOperations.TypedTuple<Point>> getPoints();
}
