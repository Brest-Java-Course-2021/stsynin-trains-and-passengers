package by.epam.brest.model.dto;

/**
 * POJO Train for model.
 */
public class TrainDto {

    /**
     * Train Id.
     */
    private Integer trainId;

    /**
     * Train name.
     */
    private String trainName;

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
     * @return Train Id.
     */
    public Integer getTrainId() {
        return trainId;
    }

    /**
     * Sets train Id.
     *
     * @param trainId train Id.
     */
    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    /**
     * Returns <code>String</code> representation of this trainName.
     *
     * @return Train name.
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
     * Returns <code>Integer</code> representation of count of passengers this train.
     *
     * @return Train's passenger count.
     */
    public Integer getTrainPassengerCount() {
        return trainPassengerCount;
    }

    /**
     * Sets train's passenger count.
     *
     * @param trainPassengerCount Train's passenger count.
     */
    public void setTrainPassengerCount(Integer trainPassengerCount) {
        this.trainPassengerCount = trainPassengerCount;
    }

    @Override
    public String toString() {
        return "TrainDto{" +
                "trainId=" + trainId +
                ", trainName='" + trainName + '\'' +
                ", trainPassengerCount=" + trainPassengerCount +
                '}';
    }
}
