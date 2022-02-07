package by.epam.brest.model.dto;

import java.time.LocalDate;

/**
 * POJO Train for model.
 */
public class TrainDto {

    /**
     * Train id.
     */
    private Integer trainId;

    /**
     * Train name.
     */
    private String trainName;

    /**
     * Train destination.
     */
    private String trainDestination;

    /**
     * Train departure date
     */
    private LocalDate trainDepartureDate;

    /**
     * Count of passengers for this train.
     */
    private Integer trainPassengerCount;

    /**
     * Constructor without arguments.
     */
    public TrainDto() {
    }

    /**
     * Constructor with train name.
     *
     * @param trainName train name.
     */
    public TrainDto(String trainName) {
        this.trainName = trainName;
    }

    /**
     * Returns <code>Integer</code> representation of this trainId.
     *
     * @return train id.
     */
    public Integer getTrainId() {
        return trainId;
    }

    /**
     * Sets train id.
     *
     * @param trainId train id.
     */
    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    /**
     * Returns <code>String</code> representation of this train name.
     *
     * @return train name.
     */
    public String getTrainName() {
        return trainName;
    }

    /**
     * Sets train name.
     *
     * @param trainName train name.
     */
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    /**
     * Returns <code>String</code> representation of this train destination.
     *
     * @return train destination.
     */
    public String getTrainDestination() {
        return trainDestination;
    }

    /**
     * Sets train destination.
     *
     * @param trainDestination Train destination.
     */
    public void setTrainDestination(String trainDestination) {
        this.trainDestination = trainDestination;
    }

    /**
     * Returns <code>LocalDate</code> representation of this train departure date.
     *
     * @return train departure date.
     */
    public LocalDate getTrainDepartureDate() {
        return trainDepartureDate;
    }

    /**
     * Sets train departure date.
     *
     * @param trainDepartureDate train departure date.
     */
    public void setTrainDepartureDate(LocalDate trainDepartureDate) {
        this.trainDepartureDate = trainDepartureDate;
    }

    /**
     * Returns <code>Integer</code> representation of count of passengers for this train.
     *
     * @return train's passengers count.
     */
    public Integer getTrainPassengerCount() {
        return trainPassengerCount;
    }

    /**
     * Sets train's passenger count.
     *
     * @param trainPassengerCount train's passengers count.
     */
    public void setTrainPassengerCount(Integer trainPassengerCount) {
        this.trainPassengerCount = trainPassengerCount;
    }

    @Override
    public String toString() {
        return "TrainDto{" +
                "trainId=" + trainId +
                ", trainName='" + trainName + '\'' +
                ", trainDestination='" + trainDestination + '\'' +
                ", trainDepartureDate=" + trainDepartureDate +
                ", trainPassengerCount=" + trainPassengerCount +
                '}';
    }
}
