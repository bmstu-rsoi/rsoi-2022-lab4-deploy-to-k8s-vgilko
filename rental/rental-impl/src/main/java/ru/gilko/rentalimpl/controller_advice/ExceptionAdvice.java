package ru.gilko.rentalimpl.controller_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gilko.rentalapi.exceptions.InvalidOperationException;
import ru.gilko.rentalapi.exceptions.NoSuchEntityException;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler({NoSuchEntityException.class})
    public ResponseEntity<?> handleException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidOperationException.class})
    public ResponseEntity<?> handleInvalidOperationException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
