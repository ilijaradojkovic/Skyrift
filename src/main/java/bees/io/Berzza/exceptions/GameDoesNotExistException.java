package bees.io.Berzza.exceptions;

public class GameDoesNotExistException extends RuntimeException{
    public GameDoesNotExistException(String message) {
        super(message);
    }
}
