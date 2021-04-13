package by.epam.brest.service.rest_app.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainNotFoundException extends RuntimeException {

    public TrainNotFoundException(String description) {
        super(description);
    }
}