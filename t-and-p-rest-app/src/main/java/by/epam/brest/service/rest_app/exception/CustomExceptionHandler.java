package by.epam.brest.service.rest_app.exception;

import by.epam.brest.dao.jdbc.exception.PassengerDuplicatedNameException;
import by.epam.brest.dao.jdbc.exception.PassengerEmptyNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Sergey Tsynin
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePassengerNotFound(PassengerNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse("PASSENGER_NOT_FOUND", e),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PassengerEmptyNameException.class)
    public ResponseEntity<ErrorResponse> handlePassengerEmptyName(PassengerEmptyNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("PASSENGER_EMPTY_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PassengerDuplicatedNameException.class)
    public ResponseEntity<ErrorResponse> handlePassengerDuplicatedName(PassengerDuplicatedNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("PASSENGER_DUPLICATED_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
