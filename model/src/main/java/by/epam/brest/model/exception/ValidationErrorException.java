package by.epam.brest.model.exception;

/**
 * @author Sergey Tsynin
 */
public class ValidationErrorException extends RuntimeException {

    public ValidationErrorException(String description) {
        super(description);
    }
}