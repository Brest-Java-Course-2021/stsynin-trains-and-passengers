package by.epam.brest.service.exception;

/**
 * @author Sergey Tsynin
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
