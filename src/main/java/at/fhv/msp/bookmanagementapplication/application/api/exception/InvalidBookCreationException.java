package at.fhv.msp.bookmanagementapplication.application.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBookCreationException extends RuntimeException {
    public InvalidBookCreationException(String message) {
        super(message);
    }

}
