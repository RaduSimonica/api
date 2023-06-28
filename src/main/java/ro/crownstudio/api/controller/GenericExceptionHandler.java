package ro.crownstudio.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.crownstudio.api.exceptions.InvalidInputDataException;
import ro.crownstudio.api.exceptions.ItemNotFoundException;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Item not be found.", e.getMessage()));
    }

    @ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputData(InvalidInputDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid input data.", e.getMessage()));
    }

    private record ErrorResponse(String error, String originalMessage) {}
}
