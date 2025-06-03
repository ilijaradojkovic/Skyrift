package bees.io.Berzza.service;


import bees.io.Berzza.domain.Bet;
import bees.io.Berzza.domain.dto.BetDTO;
import bees.io.Berzza.domain.enums.BetHighestType;
import bees.io.Berzza.domain.enums.GroupByBets;
import bees.io.Berzza.domain.requests.BetRequest;
import bees.io.Berzza.domain.responses.BetResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface BetService {
    Mono<BetResponse> placeBet(BetRequest betRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
    Mono<BetResponse>  cashOut(BetRequest betRequest ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    Mono<BetResponse> cancelBet(BetRequest betRequestMono) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    List<BetResponse> getAllBetsForUser(String username, Integer size, Integer page);
    List<Bet> getAllBetsForCurrentGame();

    List<BetDTO> getAllHighestBets(Integer size, Integer page);


    void saveBets();

    List<BetDTO> getAllBets();

//    void sendUpdateOnTopBets();
}
