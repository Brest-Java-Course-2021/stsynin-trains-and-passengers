package by.epam.brest.service;

import by.epam.brest.model.Train;

import java.util.List;
import java.util.Optional;

public interface TrainService {
    /**
     * Get all trains from the database.
     *
     * @return trains list.
     */
    List<Train> findAll();

    /**
     * Get train by Id.
     *
     * @param trainId train Id.
     * @return train.
     */
    Optional<Train> findById(Integer trainId);

    /**
     * Save new train record.
     *
     * @param train object.
     * @return saved train Id.
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
     * Delete train by Id.
     *
     * @param trainId train Id.
     * @return number of deleted trains in the database.
     */
    Integer deleteTrain(Integer trainId);

    /**
     * Get numbers of trains in the database.
     *
     * @return numbers of trains in the database.
     */
    Integer getTrainsCount();

    /**
     * Check for another train with the same name.
     *
     * @param train object.
     * @return train with the same name presence.
     */
    boolean isSecondTrainWithSameNameExists(Train train);
}
