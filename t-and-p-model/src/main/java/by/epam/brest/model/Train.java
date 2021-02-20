package by.epam.brest.model;

import java.time.LocalDate;

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
}
