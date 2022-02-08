package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class ArgumentException extends RuntimeException {

    public ArgumentException(String description) {
        super(description);
    }
}