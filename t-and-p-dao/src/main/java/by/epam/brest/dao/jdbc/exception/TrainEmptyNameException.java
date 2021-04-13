package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainEmptyNameException extends RuntimeException {

    public TrainEmptyNameException(String description) {
        super(description);
    }
}