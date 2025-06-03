package bees.io.Berzza.repository;

import bees.io.Berzza.domain.Game;
import bees.io.Berzza.domain.enums.GameState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Page<Game> findAllByGameState(GameState gameState, PageRequest pageable);
}
