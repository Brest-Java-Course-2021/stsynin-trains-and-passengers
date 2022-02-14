package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class ValidationErrorException extends RuntimeException {

    public ValidationErrorException(String description) {
        super(description);
    }
}