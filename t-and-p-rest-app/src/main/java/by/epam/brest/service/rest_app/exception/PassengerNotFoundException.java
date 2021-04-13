package by.epam.brest.service.rest_app.exception;

/**
 * @author Sergey Tsynin
 */
public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(String description){
        super(description);
    }
}
