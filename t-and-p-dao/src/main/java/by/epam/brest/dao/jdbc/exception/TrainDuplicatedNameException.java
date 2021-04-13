package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainDuplicatedNameException extends RuntimeException {

    public TrainDuplicatedNameException(String description) {
        super(description);
    }
}