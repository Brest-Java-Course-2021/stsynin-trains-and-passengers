package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class ArgumentNullException extends RuntimeException {

    public ArgumentNullException(String description) {
        super(description);
    }
}