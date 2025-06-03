package bees.io.Berzza.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

@Configuration
@RequiredArgsConstructor
public class Socket {


    private final RSocketRequester.Builder rSocketRequesterBuilder;
    private final RSocketMessageHandler rSocketMessageHandler;
//     Ovo je za testiranje soketa ka nekom drugom servisu
//    @Value("${rsocket.server.host}")
//    private String host;
//
//    @Bean
//    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
//        return builder
//                .rsocketConnector(connector -> connector
//                        .keepAlive(Duration.ofSeconds(60), Duration.ofSeconds(180)))
//                .websocket(URI.create(host));
//    }
}
