package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainOverlongDestinationNameException extends RuntimeException {

    public TrainOverlongDestinationNameException(String description) {
        super(description);
    }
}