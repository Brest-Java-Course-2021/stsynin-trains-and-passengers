package by.epam.brest.service.rest_app.exception;

import by.epam.brest.dao.jdbc.exception.*;
import by.epam.brest.model.ErrorResponse;
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

    @ExceptionHandler(PassengerOverlongNameException.class)
    public ResponseEntity<ErrorResponse> handlePassengerOverlongName(PassengerOverlongNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("PASSENGER_OVERLONG_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainLoadedException.class)
    public ResponseEntity<ErrorResponse> handleTrainLoaded(TrainLoadedException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAIN_LOADED", e),
                HttpStatus.LOCKED);
    }

    @ExceptionHandler(TrainEmptyNameException.class)
    public ResponseEntity<ErrorResponse> handleTrainEmptyName(TrainEmptyNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAIN_EMPTY_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainDuplicatedNameException.class)
    public ResponseEntity<ErrorResponse> handleTrainDuplicatedName(TrainDuplicatedNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAIN_DUPLICATED_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainOverlongNameException.class)
    public ResponseEntity<ErrorResponse> handleTrainOverlongName(TrainOverlongNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAIN_OVERLONG_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainOverlongDestinationNameException.class)
    public ResponseEntity<ErrorResponse> handleTrainOverlongName(TrainOverlongDestinationNameException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAIN_OVERLONG_DESTINATION_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainWrongFiltersOrder.class)
    public ResponseEntity<ErrorResponse> handleTrainWrongFiltersOrder(TrainWrongFiltersOrder e) {
        return new ResponseEntity<>(
                new ErrorResponse("TRAINS_WRONG_FILTER", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
