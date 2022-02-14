package by.epam.brest.model.exception;

/**
 * @author Sergey Tsynin
 */
public class ArgumentNullException extends RuntimeException {

    public ArgumentNullException(String description) {
        super(description);
    }
}