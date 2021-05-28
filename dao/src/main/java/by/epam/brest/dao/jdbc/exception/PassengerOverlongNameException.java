package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class PassengerOverlongNameException extends RuntimeException {

    public PassengerOverlongNameException(String description) {
        super(description);
    }
}
