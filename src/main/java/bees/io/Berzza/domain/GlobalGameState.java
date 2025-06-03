package bees.io.Berzza.domain;

import bees.io.Berzza.domain.enums.GameState;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class GlobalGameState {

    public static int ELEMENTS_DELAY_MS = 100;
    public static int DELAY_BETWEEN_GAMES_MS = 10000;
    public static int DELAY_ENDGAME = 5000;

    @Getter
    @Setter
    private static GameState gameState = GameState.WAITING;

    @Getter
    @Setter
    private static Game currentGame;

    @Getter
    @Setter
    private static double maxMultiplier = 1.0;

    @Getter
    @Setter
    private static double lastValue = 0.0;

    @Getter
    @Setter
    private static Long gameId = 0L;

    @Getter
    private static FluxSink<Point> fluxSing;


//    private static final DirectProcessor<Point> fluxProcessor  = DirectProcessor.create();

    @Getter
//    private static Flux<Point> gamePoints;
//    private static Sinks.Many<Point> gamePoints=Sinks.many().multicast().onBackpressureBuffer();
    private static List<Point> gamePoints = new ArrayList<>();


    static {
//        gamePoints = fluxProcessor; // gamePoints subscribes directly to fluxProcessor
//
////        gamePoints=Flux.interval(Duration.ofSeconds(1))
////                .flatMap(ignore->fluxProcessor);
////        gamePoints = Flux.create(sink -> {
////            fluxProcessor.subscribe(sink::next, sink::error, sink::complete);
////        });
//        gamePoints.subscribe();
    }

//    @Getter
//    @Setter
//    private static List<Bet> gameBets=new ArrayList<>();


    @Getter
    @Setter
    private static LocalDateTime gameStartTime;

    public static void newGame(Long counter) {
        System.out.println("NEW GAME ");
        maxMultiplier = 1;
        gameState = GameState.CREATED;
        clearGamePoints();
        gameId = counter;
    }

    public static void waitingGame() {
        System.out.println("WAITING GAME ");
        gameState = GameState.WAITING;
    }


    public static void startGame() {

        System.out.println("GAME STARTED");
        gameState = GameState.STARTED;
    }

    public static void gameEnded() {
        System.out.println("GAME ENDED");
        gameState = GameState.FINISHED;
    }

    public static void addPoint(Point point) {
//        fluxProcessor.onNext(point);
//        gamePoints.tryEmitNext(point);
        gamePoints.add(point);

    }


    public static void clearGamePoints() {
//        gamePoints=Flux.empty();
//        gamePoints=Sinks.many().multicast().onBackpressureBuffer();
        gamePoints.clear();
    }


    @Getter
    @Setter
    private static List<Bet> gameBets = new ArrayList<>();


    private static List<Bet> allGameBets=new ArrayList<>();
}
