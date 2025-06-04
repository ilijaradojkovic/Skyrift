package bees.io.Berzza.mapper;

import bees.io.Berzza.domain.Bet;

import bees.io.Berzza.domain.dto.BetDTO;
import bees.io.Berzza.domain.enums.BetType;
import bees.io.Berzza.domain.requests.BetRequest;
import bees.io.Berzza.domain.responses.BetResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BetMapper {

    public Bet mapToBet(UUID uuid, Long game, String user, double amount, boolean isAutoCashout, double autoCashOutMultiplier){

       return  Bet.builder()
               .id(uuid.toString())
                .gameId(game)
                .username(user)
                .amountBaseCurrency(amount)
               .isAutoCashout(isAutoCashout)
               .autoCashOutMultiplier(autoCashOutMultiplier)
               .timestamp(LocalDateTime.now())
                .build();
    }
    public BetResponse mapToBetResponse(UUID uuid, Long game, String user, double amount){
       return  BetResponse.builder()
               .betId(uuid.toString())
                .gameId(game)
               .email(user)
                .amount(amount)
               .cashOut(0.0)
//               .autoCashOutMultiplier()
               .time(LocalDateTime.now())
               .multiplier(0.0)
                .build();
    }
    public BetResponse mapToBetResponse(Bet bet){
       return  BetResponse.builder()
               .betId(bet.getId().toString())
                .gameId(bet.getGameId())
               .email(bet.getUsername())
               .time(bet.getTimestamp())
               .autoCashOutMultiplier(bet.getAutoCashOutMultiplier())
               .amount(bet.getAmountBaseCurrency())
               .cashOut(bet.getCashOutBaseCurrency())
               .multiplier(bet.getMultiplier())
               .isAutoCashout(bet.isAutoCashout())
                .build();
    }
    public List<BetResponse> mapToBetResponse(List<Bet> bets){
        List<BetResponse> responses=new ArrayList<>();
        for (Bet bet : bets) {
            responses.add(mapToBetResponse(bet));
        }
        return  responses;
    }

    public BetRequest mapToBetRequest(Bet bet, BetType betType, double y, double pounder) {
        return new BetRequest(bet.getAmountBaseCurrency(),bet.getId(),bet.getMultiplier(),bet.getUsername(), betType,bet.isAutoCashout(),bet.getAutoCashOutMultiplier());
    }
    public BetRequest mapToBetRequest(Bet bet,BetType betType,double multiplier) {
        return new BetRequest(bet.getAmountBaseCurrency(),bet.getId(),multiplier,bet.getUsername(), betType,bet.isAutoCashout(),bet.getAutoCashOutMultiplier());
    }

    public  List<BetDTO>  mapToBetDTO(List<Bet> allBetsForCurrentGame) {
        List<BetDTO> responses=new ArrayList<>();
        for (Bet bet : allBetsForCurrentGame) {
            responses.add(mapToBetDTO(bet));
        }
        return  responses;
    }

    public BetDTO mapToBetDTO(Bet betResponse){
           return BetDTO.builder()
                   .betId(betResponse.getId())
                    .gameId(betResponse.getGameId())
                    .userId(betResponse.getUsername())
                    .amount(betResponse.getAmountBaseCurrency())
                    .time(betResponse.getTimestamp())
                    .cashOut(betResponse.getCashOutBaseCurrency())
                    .autoCashOutMultiplier(betResponse.getAutoCashOutMultiplier())
                    .multiplier(betResponse.getMultiplier())
                    .isAutoCashout(betResponse.isAutoCashout())
                    .build();
    }
}
