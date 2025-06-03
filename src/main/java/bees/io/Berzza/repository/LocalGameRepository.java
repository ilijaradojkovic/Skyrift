package bees.io.Berzza.repository;

import bees.io.Berzza.domain.GlobalGameState;
import bees.io.Berzza.domain.enums.GameState;
import org.springframework.stereotype.Repository;

@Repository
public class LocalGameRepository {


    public GameState getGameState() {
        return GlobalGameState.getGameState();
    }
}
