package by.epam.brest.dao;

import by.epam.brest.model.Train;

import java.util.List;
import java.util.Optional;

public interface TrainDao {

    /**
     * Get all trains from the database.
     *
     * @return trains list.
     */
    List<Train> findAll();

    /**
     * Get train by id.
     *
     * @param trainId train id.
     * @return train.
     */
    Optional<Train> findById(Integer trainId);

    /**
     * Save the new train.
     *
     * @param train object.
     * @return saved train id.
     */
    Integer createTrain(Train train);

    /**
     * Update train record in the database.
     *
     * @param train object.
     * @return number of updated trains in the database.
     */
    Integer updateTrain(Train train);

    /**
     * Delete train by id.
     *
     * @param trainId train id.
     * @return number of deleted trains in the database.
     */
    Integer deleteTrain(Integer trainId);

    /**
     * Get number of trains in the database.
     *
     * @return number of trains in the database.
     */
    Integer getTrainsCount();

    /**
     * Check if this train name exists in the database.
     *
     * @param train object.
     * @return train with the same name presence.
     */
    boolean isSecondTrainWithSameNameExists(Train train);

    /**
     * Check if there are passengers on this train.
     *
     * @param trainId train Id.
     * @return the presence of passengers on this train.
     */
    boolean isTrainLoaded(Integer trainId);
}
