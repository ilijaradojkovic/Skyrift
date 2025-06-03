package bees.io.Berzza.domain.responses;

import java.io.Serializable;

public record GameHistoryResponse
        (
                Long gameId,
                double multiplier
        ) implements Serializable {
}
