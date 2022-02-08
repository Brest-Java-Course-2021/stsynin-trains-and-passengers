package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class ArgumentOutOfRangeException extends RuntimeException {

    public ArgumentOutOfRangeException(String description) {
        super(description);
    }
}