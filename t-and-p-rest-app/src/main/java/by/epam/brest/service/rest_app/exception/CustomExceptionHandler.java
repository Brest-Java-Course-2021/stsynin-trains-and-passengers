package by.epam.brest.service.rest_app.exception;

import by.epam.brest.dao.jdbc.exception.*;
import by.epam.brest.model.Acknowledgement;
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
    public ResponseEntity<Acknowledgement> handlePassengerEmptyName(PassengerEmptyNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("PASSENGER_EMPTY_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PassengerDuplicatedNameException.class)
    public ResponseEntity<Acknowledgement> handlePassengerDuplicatedName(PassengerDuplicatedNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("PASSENGER_DUPLICATED_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PassengerOverlongNameException.class)
    public ResponseEntity<Acknowledgement> handlePassengerOverlongName(PassengerOverlongNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("PASSENGER_OVERLONG_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainLoadedException.class)
    public ResponseEntity<Acknowledgement> handleTrainLoaded(TrainLoadedException e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAIN_LOADED", e),
                HttpStatus.LOCKED);
    }

    @ExceptionHandler(TrainEmptyNameException.class)
    public ResponseEntity<Acknowledgement> handleTrainEmptyName(TrainEmptyNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAIN_EMPTY_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainDuplicatedNameException.class)
    public ResponseEntity<Acknowledgement> handleTrainDuplicatedName(TrainDuplicatedNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAIN_DUPLICATED_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainOverlongNameException.class)
    public ResponseEntity<Acknowledgement> handleTrainOverlongName(TrainOverlongNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAIN_OVERLONG_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainOverlongDestinationNameException.class)
    public ResponseEntity<Acknowledgement> handleTrainOverlongName(TrainOverlongDestinationNameException e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAIN_OVERLONG_DESTINATION_NAME", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TrainWrongFiltersOrder.class)
    public ResponseEntity<Acknowledgement> handleTrainWrongFiltersOrder(TrainWrongFiltersOrder e) {
        return new ResponseEntity<>(
                new Acknowledgement("TRAINS_WRONG_FILTER", e),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
