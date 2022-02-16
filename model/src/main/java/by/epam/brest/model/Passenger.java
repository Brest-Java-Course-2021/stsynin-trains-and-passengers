package by.epam.brest.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;

public class Passenger {

    private Integer passengerId;

    @NotBlank(message = "Passenger name can't be empty")
    @Size(max = MAX_PASSENGER_NAME_LENGTH, message = "This name is too long")
    private String passengerName;

    private Integer trainId;

    public Passenger() {
    }

    public Passenger(String passengerName) {
        this.passengerName = passengerName;
    }

    public Passenger(Integer passengerId, String passengerName, Integer trainId) {
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.trainId = trainId;
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
        return "Passenger{" +
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
