package bees.io.Berzza.exceptions;

public class UserIncorrectPasswordException extends RuntimeException{
    public UserIncorrectPasswordException(String message) {
        super(message);
    }
}
