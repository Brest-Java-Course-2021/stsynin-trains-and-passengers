package by.epam.brest.model;

public class Passenger {

    private Integer passengerId;

    private String passengerName;

    private Train train;

    public Passenger() {
    }

    public Passenger(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
