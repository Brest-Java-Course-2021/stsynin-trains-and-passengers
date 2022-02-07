package by.epam.brest.model;

import java.time.LocalDate;
import java.util.Objects;

public class Train {

    private Integer trainId;

    private String trainName;

    private String trainDestination;

    private LocalDate trainDepartureDate;

    public Train() {
    }

    public Train(String trainName) {
        this.trainName = trainName;
    }

    public Train(Integer trainId, String trainName, String trainDestination, LocalDate trainDepartureDate) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainDestination = trainDestination;
        this.trainDepartureDate = trainDepartureDate;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainDestination() {
        return trainDestination;
    }

    public void setTrainDestination(String trainDestination) {
        this.trainDestination = trainDestination;
    }

    public LocalDate getTrainDepartureDate() {
        return trainDepartureDate;
    }

    public void setTrainDepartureDate(LocalDate trainDepartureDate) {
        this.trainDepartureDate = trainDepartureDate;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId=" + trainId +
                ", trainName='" + trainName + '\'' +
                ", trainDestination='" + trainDestination + '\'' +
                ", trainDepartureDate=" + trainDepartureDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return Objects.equals(trainId, train.trainId) &&
                Objects.equals(trainName, train.trainName) &&
                Objects.equals(trainDestination, train.trainDestination) &&
                Objects.equals(trainDepartureDate, train.trainDepartureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainId, trainName, trainDestination, trainDepartureDate);
    }
}
