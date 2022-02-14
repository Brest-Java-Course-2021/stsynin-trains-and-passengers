package by.epam.brest.model.exception;

/**
 * @author Sergey Tsynin
 */
public class ArgumentOutOfRangeException extends RuntimeException {

    public ArgumentOutOfRangeException(String description) {
        super(description);
    }
}