package bees.io.Berzza.exceptions;

import bees.io.Berzza.domain.dto.FailedRestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {



    @ExceptionHandler({

            UserDoesNotExistException.class,
            AdminNotFoundException.class,
            UserIncorrectPasswordException.class,
            AccountAlreadyExists.class,


    })
    public ResponseEntity<FailedRestResponse> handle(RuntimeException runtimeException) {

        return ResponseEntity.badRequest().body(new FailedRestResponse(runtimeException.getMessage(), HttpStatus.BAD_REQUEST));
    }
}
