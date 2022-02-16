package by.epam.brest.dao;

import by.epam.brest.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerDao {

    /**
     * Get all passengers from the database.
     *
     * @return passengers list.
     */
    List<Passenger> findAll();

    /**
     * Get passenger by id.
     *
     * @param passengerId passenger id.
     * @return passenger.
     */
    Optional<Passenger> findById(Integer passengerId);

    /**
     * Save the new passenger.
     *
     * @param passenger object.
     * @return saved passenger id.
     */
    Integer createPassenger(Passenger passenger);

    /**
     * Update the passenger record in the database.
     *
     * @param passenger object.
     * @return number of updated passengers in the database.
     */
    Integer updatePassenger(Passenger passenger);

    /**
     * Delete passenger by id.
     *
     * @param passengerId passenger id.
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
     * Check if this passenger name exists in the database.
     *
     * @param passenger object.
     * @return passenger with the same name presence.
     */
    boolean isSecondPassengerWithSameNameExists(Passenger passenger);
}
