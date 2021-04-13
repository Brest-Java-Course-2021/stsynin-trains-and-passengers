package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainLoadedException extends RuntimeException {

    public TrainLoadedException(String description) {
        super(description);
    }
}