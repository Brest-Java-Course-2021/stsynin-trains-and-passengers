package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class PassengerEmptyNameException extends RuntimeException {

    public PassengerEmptyNameException(String description) {
        super(description);
    }
}