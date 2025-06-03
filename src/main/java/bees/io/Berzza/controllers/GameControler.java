package bees.io.Berzza.controllers;

import bees.io.Berzza.domain.GlobalGameState;
import bees.io.Berzza.domain.Point;
import bees.io.Berzza.domain.enums.GameState;
import bees.io.Berzza.domain.responses.GameHistoryResponse;
import bees.io.Berzza.domain.responses.GameStateResponse;
import bees.io.Berzza.service.GameService;
import bees.io.Berzza.service.JwtService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static bees.io.Berzza.domain.GlobalGameState.ELEMENTS_DELAY_MS;

@RestController
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = {"http://localhost:8000","http://localhost:8080"})
public class GameControler {

    private final GameService gameService;
    private final JwtService jwtService;

    @MessageMapping("game-state")
    public Flux<GameStateResponse> gameState() {
        log.info("Getting game state");

        return Flux.interval(Duration.ofMillis(ELEMENTS_DELAY_MS))
                .map(x -> {

                    GameState gameState = gameService.getGameState();
                    long secondsPassed = Duration.between(GlobalGameState.getGameStartTime(), LocalDateTime.now()).getSeconds();


                    return new GameStateResponse(gameState, secondsPassed, GlobalGameState.getLastValue(), GlobalGameState.getGameId());
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while getting game state: {}", e.getMessage(), e);
                    // Vraća alternativni reaktivni tok
                    return Flux.just(new GameStateResponse(null, 0, GlobalGameState.getLastValue(), GlobalGameState.getGameId()));
                });
    }

    @MessageMapping("points")
    public Flux<Point> getPoints() {
        log.info("Getting points");

        return Flux.interval(Duration.ofMillis(ELEMENTS_DELAY_MS)) // Emit points every ELEMENTS_DELAY_MS milliseconds
                .flatMap(ignore -> gameService.getPoints()) // Pretpostavljam da gameService.getPoints() vraća Flux<Point>
                .distinct(x -> Objects.requireNonNull(x.getValue()).getId())
                .map(ZSetOperations.TypedTuple::getValue)
                .onErrorResume(e -> {
                    // Možete se odlučiti za vraćanje praznog toka ili podrazumevanih vrednosti
                    log.warn("Returning default points due to error");
                    return Flux.empty(); // Vraća prazan Flux
                });
    }

    @GetMapping("/api/games/history")
    @Retry(name = "gameController")
    @RateLimiter(name = "gameController")
//    @Cacheable(value = "gamesHistory", key = "T(bees.io.Berzza.domain.GlobalGameState).getGameState()")
    public List<GameHistoryResponse> getGameHistory(
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("Getting game history request");
        List<GameHistoryResponse> history = gameService.getGameHistory(size);
        log.info("Successfully retrieved game history of size: {}", history.size());
        return history;
    }

    @GetMapping("/test")
    public String test() {
        log.info("test");
        return "test";
    }

}
