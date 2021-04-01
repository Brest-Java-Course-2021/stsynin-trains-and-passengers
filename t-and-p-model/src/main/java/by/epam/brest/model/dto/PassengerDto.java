package by.epam.brest.model.dto;

/**
 * POJO Train for model.
 */
public class PassengerDto {

    /**
     * Passenger Id.
     */
    private Integer passengerId;

    /**
     * Passenger name.
     */
    private String passengerName;

    /**
     * Train Id.
     */
    private String passengerTrainName;

    /**
     * Constructor without arguments.
     */
    public PassengerDto() {
    }

    /**
     * Constructor with passenger name.
     *
     * @param passengerName Passenger name.
     */
    public PassengerDto(String passengerName) {
        this.passengerName = passengerName;
    }

    /**
     * Returns <code>Integer</code> representation of this passengerId.
     *
     * @return passenger Id.
     */
    public Integer getPassengerId() {
        return passengerId;
    }

    /**
     * Sets passenger Id.
     *
     * @param passengerId passenger Id.
     */
    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    /**
     * Returns <code>String</code> representation of this passenger name.
     *
     * @return passenger name.
     */
    public String getPassengerName() {
        return passengerName;
    }

    /**
     * Sets passenger name.
     *
     * @param passengerName passenger name.
     */
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    /**
     * Returns <code>String</code> representation of this passenger train name.
     *
     * @return passenger train name.
     */
    public String getPassengerTrainName() {
        return passengerTrainName;
    }

    /**
     * Sets passenger train name.
     *
     * @param passengerTrainName passenger train name.
     */
    public void setPassengerTrainName(String passengerTrainName) {
        this.passengerTrainName = passengerTrainName;
    }

    @Override
    public String toString() {
        return "PassengerDto{" +
                "passengerId=" + passengerId +
                ", passengerName='" + passengerName + '\'' +
                ", passengerTrainName='" + passengerTrainName + '\'' +
                '}';
    }
}
