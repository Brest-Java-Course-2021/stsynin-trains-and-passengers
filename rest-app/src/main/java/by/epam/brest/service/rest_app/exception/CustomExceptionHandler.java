package by.epam.brest.service.rest_app.exception;

import by.epam.brest.model.exception.ValidationErrorException;
import by.epam.brest.model.exception.ArgumentNullException;
import by.epam.brest.model.exception.ArgumentOutOfRangeException;
import by.epam.brest.service.exception.ResourceLockedException;
import by.epam.brest.model.ErrorMessage;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Sergey Tsynin
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    public CustomExceptionHandler() {
        LOGGER.info("CustomExceptionHandler was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(ResourceNotFoundException e) {
        LOGGER.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ResourceLockedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleTrainLoaded(ResourceLockedException e) {
        LOGGER.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ArgumentNullException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handleArgumentNullException(ArgumentNullException e) {
        LOGGER.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ValidationErrorException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handleArgumentException(ValidationErrorException e) {
        LOGGER.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ArgumentOutOfRangeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handleArgumentOutOfRangeException(ArgumentOutOfRangeException e) {
        LOGGER.error(e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleUnknownExceptions(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return new ErrorMessage("Happened unknown error!");
    }
}
