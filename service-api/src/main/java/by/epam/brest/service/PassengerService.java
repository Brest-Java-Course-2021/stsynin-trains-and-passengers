package by.epam.brest.service;

import by.epam.brest.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerService {

    /**
     * Get all passengers from the database.
     *
     * @return passengers list.
     */
    List<Passenger> findAll();

    /**
     * Get passenger by Id.
     *
     * @param passengerId passenger Id.
     * @return passenger.
     */
    Optional<Passenger> findById(Integer passengerId);

    /**
     * Save new passenger record.
     *
     * @param passenger object.
     * @return saved passenger Id.
     */
    Integer createPassenger(Passenger passenger);

    /**
     * Update passenger record in the database.
     *
     * @param passenger object.
     * @return number of updated passengers in the database.
     */
    Integer updatePassenger(Passenger passenger);

    /**
     * Delete passenger by Id.
     *
     * @param passengerId train Id.
     * @return number of deleted passengers in the database.
     */
    Integer deletePassenger(Integer passengerId);

    /**
     * Get number of passengers in the database.
     *
     * @return number of passengers in the database.
     */
    Integer getPassengersCount();

    /**
     * Check if this name of passenger is exist in the database.
     */
    boolean isSecondPassengerWithSameNameExists(Passenger passenger);
}
