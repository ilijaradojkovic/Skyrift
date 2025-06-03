package bees.io.Berzza.repository;

import bees.io.Berzza.domain.GlobalGameState;
import bees.io.Berzza.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisGameRepository {
    private final ReactiveRedisTemplate<String, Point> redisTemplate;

    public Mono<Void> savePoints(Point point, Long id) {
        return Mono.when(
                redisTemplate.opsForZSet().add(id.toString(), point, point.getX()));
    }

    public Flux<ZSetOperations.TypedTuple<Point>> getPoints() {
        return redisTemplate.opsForZSet().rangeWithScores(GlobalGameState.getGameId().toString(), Range.unbounded());
    }

    public void deleteGame() {
        redisTemplate.opsForZSet().delete(GlobalGameState.getGameId().toString()).subscribe();
    }

}
