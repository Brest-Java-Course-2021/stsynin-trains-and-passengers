package by.epam.brest.service;

import by.epam.brest.model.Train;

import java.util.List;

public interface TrainService {
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
    Train findById(Integer trainId);

    /**
     * Save the new train.
     *
     * @param train object.
     * @return new train id.
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
    Integer deleteById(Integer trainId);

    /**
     * Get count of trains in the database.
     *
     * @return count of trains in the database.
     */
    Integer getTrainsCount();
}
