package by.epam.brest.dao.jdbc.exception;

/**
 * @author Sergey Tsynin
 */
public class TrainWrongFiltersOrder extends RuntimeException {

    public TrainWrongFiltersOrder(String description) {
        super(description);
    }
}
