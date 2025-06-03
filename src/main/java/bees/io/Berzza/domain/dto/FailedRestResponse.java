package bees.io.Berzza.domain.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class FailedRestResponse extends Response {
    public FailedRestResponse(String message,HttpStatus httpStatus) {
        super(LocalDateTime.now(), httpStatus.value(), httpStatus, null, message, null, null);
    }

}
