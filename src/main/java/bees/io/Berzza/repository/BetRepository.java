package bees.io.Berzza.repository;

import bees.io.Berzza.domain.Bet;
import bees.io.Berzza.domain.GlobalGameState;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet,Long> {

//    long countByGameIdAndUserId(Long gameId, String userId);

    List<Bet> findAllByUsernameOrderByTimestampDesc(String username, PageRequest of);

    default  List<Bet> getBets(){
        return GlobalGameState.getGameBets();
    }

//    List<Bet> findAllByGame(Long game, PageRequest of);

    List<Bet> findTop20ByOrderByCashOutBaseCurrencyDesc();

    //    List<Bet> findAllByGameAndUser(Long game, User user, PageRequest of);
//    List<Bet> findAllByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end , PageRequest of);
//    List<Bet> findAllByUser(User user, PageRequest of);
    List<Bet> findAllByTimestampBetweenAndAlreadyCashedOut(LocalDateTime start,LocalDateTime end,Boolean alreadyCashOut,PageRequest of);

}
