package br.com.fiap.globalsolution.globalsight_api.exception;

// import org.springframework.http.HttpStatus; // No longer needed here
// import org.springframework.web.bind.annotation.ResponseStatus; // No longer needed here

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
