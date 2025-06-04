package bees.io.Berzza.service.impl;


import bees.io.Berzza.domain.Bet;
import bees.io.Berzza.domain.GlobalCasinoConfiguration;
import bees.io.Berzza.domain.GlobalGameState;
import bees.io.Berzza.domain.dto.BetDTO;
import bees.io.Berzza.domain.enums.GameState;
import bees.io.Berzza.domain.requests.BetRequest;
import bees.io.Berzza.domain.responses.BetResponse;
import bees.io.Berzza.exceptions.*;
import bees.io.Berzza.mapper.BetMapper;
import bees.io.Berzza.repository.BetRepository;
import bees.io.Berzza.repository.GameRepository;
import bees.io.Berzza.service.BetService;
import bees.io.Berzza.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final GameRepository gameRepository;
    private final BetRepository betRepositry;
    private final BetMapper betMapper;
    private final WebClient webClient;
    private  final UserService userService;



    @Override
    public Mono<BetResponse> placeBet(BetRequest betRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        return Mono.defer(() -> {


            Long game = GlobalGameState.getGameId();
            GameState gameState = GlobalGameState.getGameState();
            if (gameState != GameState.WAITING) {
                return Mono.error(new InvalidGameStateException("Game with id " + game + " is not in waiting state"));
            }

            if(GlobalCasinoConfiguration.minBet>betRequest.amount()){
                return Mono.error(new MinBetException("Min bet is "+GlobalCasinoConfiguration.minBet));
            }

            if(GlobalCasinoConfiguration.maxBet<betRequest.amount()){
                return Mono.error(new MaxBetException("Max bet is "+GlobalCasinoConfiguration.maxBet));
            }

            Bet bet = betMapper.mapToBet(UUID.randomUUID(), game, betRequest.username(), betRequest.amount(), betRequest.isAutoCashout(), betRequest.autoCashOutMultiplier());

            GlobalGameState.getGameBets().add(bet);
            userService.removeFromBalance(betRequest.username(), betRequest.amount());
            System.out.println("BET SAVED " + bet);
//            return casinoRest.cashIn(userToken, betRequest.amount())
//                    .flatMap(userDTO -> {
//                        user.setBalance(userDTO.getBalance());
//                        userService.update(user);
//
//
//                    });
            return Mono.just(betMapper.mapToBetResponse(bet));
        });
    }

    @Override
    public Mono<BetResponse> cashOut(BetRequest betRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        return Mono.defer(() -> {

            Long game = GlobalGameState.getGameId();
            GameState gameState = GlobalGameState.getGameState();
            if (gameState != GameState.STARTED)
                throw new InvalidGameStateException("Game with id " + game + " did not start yet");


            Bet bet = GlobalGameState.getGameBets().stream().filter(x -> x.getId().equals(betRequest.betId())).findFirst().orElseThrow(() -> new BetDoesNotExistException("Bet with id " + betRequest.betId() + " does not exist"));;

            if (bet.isAlreadyCashedOut())
                return Mono.error(new IllegalStateException("Bet with id " + betRequest.betId() + " has already been cashed out"));


            System.out.println(betRequest);
            double moneyInBaseCurrency = bet.getAmountBaseCurrency() * betRequest.multiplier() ;
            double profitInBaseCurrency = moneyInBaseCurrency - bet.getAmountBaseCurrency();
            //Max win i ostale stvari su striktno vezane za kazino pa sa njihovom currency uporedjeujemo
            if(moneyInBaseCurrency>GlobalCasinoConfiguration.maxWin){
                moneyInBaseCurrency=GlobalCasinoConfiguration.maxWin;
                //Ovde bi trebalo da se izvuce iz baze koliko je to u base currency i da se sracuna
                profitInBaseCurrency=GlobalCasinoConfiguration.maxWin;
            }
            bet.setMultiplier(betRequest.multiplier());
            bet.setProfitBaseCurrency(profitInBaseCurrency);
            bet.setCashOutBaseCurrency(moneyInBaseCurrency);
            bet.setAlreadyCashedOut(true);
            userService.addToBalance(bet.getUsername(), moneyInBaseCurrency);

            System.out.println("ALL BETS " +GlobalGameState.getGameBets());
//            return casinoRest.cashOut(userToken, bet.getCashOut())
//                    .flatMap(userDTO -> {
//                        user.setBalance(userDTO.getBalance());
//                        userService.update(user);
//                        return Mono.just(betMapper.mapToBetResponse(bet));
//                    });

            return Mono.just(betMapper.mapToBetResponse(bet));
        });

    }

    @Override
    public Mono<BetResponse> cancelBet(BetRequest betRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {


        return Mono.defer(
                () -> {
                    // Find the bet
                    Bet bet = GlobalGameState.getGameBets().stream()
                            .filter(x -> x.getId().equals(betRequest.betId()))
                            .findFirst()
                            .orElseThrow(() -> new BetDoesNotExistException("Bet with id " + betRequest.betId() + " does not exist"));

                    // Find the user by token

                    // Perform the delete request before removing the bet
                    return Mono.defer(() -> {
                                // Remove the bet from the global state
                                GlobalGameState.getGameBets().remove(bet);

                                // Call the casinoRest API to update user balance
//                                return casinoRest.cancelBet(bet.getAmountCasinoCurrency(), userToken).flatMap(userDTO -> {
//                                    user.setBalance(userDTO.getBalance());
//                                    userService.update(user);
//                                    return Mono.just(betMapper.mapToBetResponse(bet));
//                                });
                                userService.addToBalance(bet.getUsername(), bet.getAmountBaseCurrency());
                                return Mono.just(betMapper.mapToBetResponse(bet));

                            }
                    );

                }
        );
    }






    @Override
    public List<BetResponse> getAllBetsForUser(String username, Integer size, Integer page) {

        return betMapper.mapToBetResponse(betRepositry.findAllByUsernameOrderByTimestampDesc(username, PageRequest.of(page, size)));

    }

    @Override
    public List<Bet> getAllBetsForCurrentGame() {
        return betRepositry.getBets();
    }

    @Override
    public List<BetDTO> getAllHighestBets(Integer size, Integer page) {

        return betMapper.mapToBetDTO(betRepositry.findTop20ByOrderByCashOutBaseCurrencyDesc());
    }

        @Override
        public void saveBets () {
            for (Bet gameBet : GlobalGameState.getGameBets()) {
                betRepositry.save(gameBet);
            }
            GlobalGameState.getGameBets().clear();
        }

        @Override
        public List<BetDTO> getAllBets () {
            return betMapper.mapToBetDTO(GlobalGameState.getGameBets());
        }

//        @Override
//        public void sendUpdateOnTopBets () {
//            List<BetDTO> betDTOS = betMapper.mapToBetDTO(GlobalGameState.getGameBets());
//            System.out.println(betDTOS);
//            if (!GlobalGameState.getGameBets().isEmpty())
//                webClient.post()
//                        .uri("lb://"+wrapperURL + "/api/bets/game-finished")
//                //jer ovde saljemo na onaj wrapper mi moramo da saljemo base currency
//                        .body(BodyInserters.fromValue(betDTOS))
//                        .retrieve()
//                        .toBodilessEntity()
//                        .subscribe();
//
//        }

    }
