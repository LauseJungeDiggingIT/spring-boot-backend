package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateMobileNumberException extends RuntimeException {
    public DuplicateMobileNumberException(String message) {
        super(message);
    }
}