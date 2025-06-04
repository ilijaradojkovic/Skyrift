package bees.io.Berzza.controllers;

import bees.io.Berzza.domain.dto.BetDTO;
import bees.io.Berzza.domain.dto.NewUserDTO;
import bees.io.Berzza.domain.dto.SuccessRestResponse;
import bees.io.Berzza.domain.enums.BetHighestType;
import bees.io.Berzza.domain.enums.BetType;
import bees.io.Berzza.domain.enums.GroupByBets;
import bees.io.Berzza.domain.requests.BetRequest;
import bees.io.Berzza.domain.responses.BetResponse;
import bees.io.Berzza.mapper.BetMapper;
import bees.io.Berzza.service.BetService;
import bees.io.Berzza.service.JwtService;

import bees.io.Berzza.util.CurrencyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;
    private final BetMapper betMapper;
    private final JwtService jwtService;
    private final WebClient webClient;

    //jedan socket za betovanje po useru
//    @MessageMapping("bet")
//    public Mono<BetResponse> bet(@Payload BetRequest betRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//        if (betRequest.betType() == BetType.BET) {
//            System.out.println("Place bet");
//            return betService.placeBet(betRequest);
//        } else if (betRequest.betType() == BetType.CANCEL) {
//            System.out.println("Cancel bet");
//            Mono<BetResponse> betResponseMono = betService.cancelBet(betRequest);
//
//            return  betResponseMono;
//        } else if (betRequest.betType() == BetType.CASHOUT){
//            System.out.println("Cash out bet");
//            return betService.cashOut(betRequest);
//        }
//          return Mono.empty();
//    }
//




    @GetMapping("/my-bets")
    public SuccessRestResponse getMyBetsForGame(
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam("user") NewUserDTO user
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Get all bets for user");
        List<BetResponse> allBetsForUser = betService.getAllBetsForUser(user.username(), size, page);
        return new SuccessRestResponse("All bets for user", Map.of("myBets",allBetsForUser));
    }


    //We use this endpoint to comunicate with Client
    @GetMapping("/live")
    public SuccessRestResponse getCurrentBets(){

        //We get currency in EUR(BASE) and we need to convert it in currency that user has
        List<BetDTO> allBets = betService.getAllBets();
        return new SuccessRestResponse("All bets", Map.of("bets", allBets.stream().map(CurrencyUtil::mapToCasinoCurrency
        ).collect(Collectors.toList())));
    }



    @GetMapping("/highest")
    public List<BetDTO> getALlHighestets(
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            @RequestParam(value = "page",defaultValue = "0") Integer page

    ){
        System.out.println("Get all highest bets");
        return betService.getAllHighestBets(size,page);
    }

//    @GetMapping("/api/bets/count/currentGame")
//    @CrossOrigin(origins = {"http://localhost:3000","http://localhost:8000","http://localhost:8080","http://localhost:5173"})
//    public Integer getCountBetsForCurrentGame(){
//        return  betRedisRepository.getBets().count().
//    }
}
