package by.epam.brest.service.rest_app.exception;

/**
 * @author Sergey Tsynin
 */
public class ErrorResponse {

    private String message;

    private String descriptions;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, String descriptions) {
        this.message = message;
        this.descriptions = descriptions;
    }

    public ErrorResponse(String message, Exception e) {
        this.message = message;
        this.descriptions = e.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}