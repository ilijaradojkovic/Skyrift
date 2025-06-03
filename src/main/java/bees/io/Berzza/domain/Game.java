package bees.io.Berzza.domain;

import bees.io.Berzza.domain.enums.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gamehash;
    @Enumerated(EnumType.STRING)
    private GameState gameState;

    @OneToOne
    private Game nextGame;
    private double maxMultiplier;
    private int breakingPoints;


    @Transient
    private List<Point> runListPoints;

    @Transient
    private List<Double> runList;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private String runListJson; // Serialized runList as JSON string

    @CreatedDate
    private LocalDateTime createdAt;

    // Method to serialize runList to JSON string
    public void serializeRunList() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.runListJson = objectMapper.writeValueAsString(this.runListPoints);
    }

    // Method to deserialize JSON string to runList
    public void deserializeRunList() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.runListPoints = objectMapper.readValue(this.runListJson, new TypeReference<List<Point>>() {
        });
    }
}
