package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainOverlongNameException extends RuntimeException {

    public TrainOverlongNameException(String description) {
        super(description);
    }
}