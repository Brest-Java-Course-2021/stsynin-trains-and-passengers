package by.epam.brest.service.exception;

/**
 * @author Sergey Tsynin
 */
public class ResourceLockedException extends RuntimeException {

    public ResourceLockedException(String description) {
        super(description);
    }
}