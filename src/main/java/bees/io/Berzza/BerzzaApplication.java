package bees.io.Berzza;

import bees.io.Berzza.service.GameService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.DirectProcessor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAsync
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy
public class BerzzaApplication implements ApplicationRunner {

    private final GameService gameService;
    //	private final RSocketRequester rSocketRequester;
    private final LettuceConnectionFactory redisConnectionFactory;

    public static void main(String[] args) {
        SpringApplication.run(BerzzaApplication.class, args);
    }


    //	private static FluxSink<String> fluxSing;
    private static DirectProcessor<String> fluxProcessor = DirectProcessor.create();

    @PostConstruct
    public void logRedisConfig() {
        System.out.println("Redis Host: " + redisConnectionFactory.getHostName());
        System.out.println("Redis Port: " + redisConnectionFactory.getPort());
        System.out.println("Redis Host: " + System.getenv("REDIS_HOST"));
        System.out.println("Redis Port: " + System.getenv("REDIS_PORT"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        gameService.generateGames();


    }
}
