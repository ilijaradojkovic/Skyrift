package bees.io.Berzza.domain.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class SuccessRestResponse extends Response{
    public SuccessRestResponse(String message, Map<?,?> data) {
        super(LocalDateTime.now(), HttpStatus.OK.value(), HttpStatus.OK,null,  message,null, data);
    }
    public SuccessRestResponse(String message) {
        super(LocalDateTime.now(), HttpStatus.OK.value(), HttpStatus.OK,null,  message,null, null);
    }
}
