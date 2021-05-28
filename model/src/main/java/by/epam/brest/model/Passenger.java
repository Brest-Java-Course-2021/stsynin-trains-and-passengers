package by.epam.brest.model;

import java.util.Objects;

public class Passenger {

    private Integer passengerId;

    private String passengerName;

    private Integer trainId;

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

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    @Override
    public String toString() {
        return "\n\rPassenger{" +
                "passengerId=" + passengerId +
                ", passengerName='" + passengerName + '\'' +
                ", trainId=" + trainId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(passengerId, passenger.passengerId) &&
                Objects.equals(passengerName, passenger.passengerName) &&
                Objects.equals(trainId, passenger.trainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId, passengerName, trainId);
    }
}
