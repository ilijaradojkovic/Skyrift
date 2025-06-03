package bees.io.Berzza.service.impl;

import bees.io.Berzza.domain.Game;
import bees.io.Berzza.domain.GameGenerator;
import bees.io.Berzza.domain.GlobalGameState;
import bees.io.Berzza.domain.Point;
import bees.io.Berzza.domain.enums.GameState;
import bees.io.Berzza.domain.responses.GameHistoryResponse;
import bees.io.Berzza.mapper.GameMapper;
import bees.io.Berzza.repository.GameRepository;
import bees.io.Berzza.repository.RedisGameRepository;
import bees.io.Berzza.service.BetService;
import bees.io.Berzza.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static bees.io.Berzza.domain.GlobalGameState.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {


    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final RedisGameRepository redisGameRepository;
    private final BetService betService;

    private Game currentGame;

    @Value("${game.generateNumber:10}")
    private int gamesToGenerate;
    private static final double INCREMENT_X = 1;
    private static final double PONDER_LOW = 0.5;
    private static final double PONDER_HIGH = 1;

    @Override
    @Transactional
    public void generateGames() {

        Flux<Game> games = Flux.generate(
                () -> 1,
                (counter, sink) -> {
                    System.out.println("Generating game " + counter);
                    Game game = GameGenerator.interpolateList(GameGenerator.generateListFromMaxIncreasingOnly(GameGenerator.generateMaxMultiplier()));
                    game.setId(counter.longValue());
                    game.setCreatedAt(LocalDateTime.now());


                    //Save in PG
                    gameRepository.save(game);

                    GlobalGameState.newGame(game.getId());
                    sink.next(game);

                    setUpNewCreatedGame(game);

                    return counter + 1;
                }
        );


        games.concatMap(game -> {
                    // Log before calling generatePoints
                    // System.out.println("Before generatePoints");
                    return generatePoints(game)
                            .doOnSubscribe(sub -> doAtStart(game))
                            .delaySubscription(Duration.ofMillis(DELAY_BETWEEN_GAMES_MS))
                            .doOnComplete(() -> System.out.println("Complete generatePoints for game: " + game)) // Log completion
                            .then(Mono.delay(Duration.ofMillis(500)))  // Adding 1 second delay after completion
                            .doOnTerminate(() -> doAfterEnd(game))// doAfterEnd called after delay
                            .then(Mono.defer(() -> Mono.delay(Duration.ofMillis(DELAY_ENDGAME))));

                })
                .subscribe(nextgame -> {

                });
    }

    private Flux<Object> generatePoints(Game game) {
        AtomicReference<Double> ponder = new AtomicReference<Double>(0.0);
        AtomicReference<Double> x = new AtomicReference<Double>(0.0);

        return Flux.range(0, game.getRunList().size())
                .delayElements(Duration.ofMillis(ELEMENTS_DELAY_MS))
                //concatmap nije hteo lepo da radi??
                .flatMap(i -> {
                    if (i > 0) {
                        if (game.getRunList().get(i) > game.getRunList().get(i - 1))
                            ponder.set(PONDER_HIGH);
                        else
                            ponder.set(PONDER_LOW);
                    }
                    x.set(x.get() + INCREMENT_X);

                    Point point = new Point(UUID.randomUUID().toString(), x.get(), game.getRunList().get(i), ponder.get());

                    if (game.getRunList().get(i) > GlobalGameState.getMaxMultiplier()) {
                        GlobalGameState.setMaxMultiplier(game.getMaxMultiplier());
                    }

                    GlobalGameState.addPoint(point);

                    return redisGameRepository.savePoints(point, GlobalGameState.getGameId());
                });
    }

    private void doAtStart(Game game) {
        game.setGameState(GameState.STARTED);
        GlobalGameState.startGame();

    }


    @Transactional
    private void doAfterEnd(Game game) {
        GlobalGameState.setLastValue(game.getRunList().get(game.getRunList().size() - 1));
//        saveBets();
        redisGameRepository.deleteGame();
        GlobalGameState.gameEnded();
        game.setGameState(GameState.FINISHED);
        game.setMaxMultiplier(GlobalGameState.getMaxMultiplier());
        game.setRunListPoints(GlobalGameState.getGamePoints());
        betService.saveBets();
        try {
            game.serializeRunList();
            gameRepository.save(game);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void setUpNewCreatedGame(Game game) {
        game.setGameState(GameState.WAITING);
        GlobalGameState.setGameStartTime(LocalDateTime.now());
        GlobalGameState.waitingGame();
    }

    @Override
    public List<GameHistoryResponse> getGameHistory(Integer size) {
        log.info("Getting game history with size: {}", size); // Koristi placeholder za logovanje
        List<Game> topNByOrderByCreatedAtDesc = gameRepository.findAllByGameState(GameState.FINISHED, PageRequest.of(0, size, Sort.by(Sort.Order.desc("createdAt")))).getContent();
        return gameMapper.toGameHistoryResponses(topNByOrderByCreatedAtDesc);
    }

    @Override
    public GameState getGameState() {
        log.debug("Fetching current game state");
        return GlobalGameState.getGameState();
    }

    @Override
    public Flux<ZSetOperations.TypedTuple<Point>> getPoints() {
        log.debug("Fetching points");
        return redisGameRepository.getPoints();
    }


}
