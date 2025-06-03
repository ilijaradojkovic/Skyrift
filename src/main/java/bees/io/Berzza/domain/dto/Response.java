package bees.io.Berzza.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    protected LocalDateTime timestamp;
    protected int statusCode;
    protected HttpStatus httpStatus;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected String errorCode;
    protected Map<?,?> data;

    public Response(LocalDateTime timestamp, int statusCode, HttpStatus httpStatus, String reason, String message, String errorCode, Map<?,?> data) {
        this.timestamp=timestamp;
        this.statusCode=statusCode;
        this.httpStatus=httpStatus;
        this.reason=reason;
        this.message=message;
        this.errorCode=errorCode;
        this.data=data;

    }
}
