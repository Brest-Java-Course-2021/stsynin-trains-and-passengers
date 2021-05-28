package by.epam.brest.model;

/**
 * @author Sergey Tsynin
 */
public class Acknowledgement {

    private String message;

    private String descriptions;

    private Integer id;

    public Acknowledgement() {
    }

    public Acknowledgement(String message, String descriptions) {
        this.message = message;
        this.descriptions = descriptions;
    }

    public Acknowledgement(String message, String descriptions, Integer id) {
        this.message = message;
        this.descriptions = descriptions;
        this.id = id;
    }

    public Acknowledgement(String message, Exception e) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}