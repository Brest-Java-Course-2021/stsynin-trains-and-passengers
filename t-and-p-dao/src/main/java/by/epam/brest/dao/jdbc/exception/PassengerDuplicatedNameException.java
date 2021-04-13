package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class PassengerDuplicatedNameException extends RuntimeException {

    public PassengerDuplicatedNameException(String description) {
        super(description);
    }
}
