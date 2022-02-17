package by.epam.brest.service;

import by.epam.brest.model.Passenger;

import java.util.List;

public interface PassengerService {

    /**
     * Get all passengers list.
     *
     * @return passengers list.
     */
    List<Passenger> findAll();

    /**
     * Get passenger by id.
     *
     * @param id passenger id.
     * @return passenger.
     */
    Passenger findById(Integer id);

    /**
     * Save the new passenger.
     *
     * @param passenger object.
     * @return new passenger id.
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
     * Delete passenger by id.
     *
     * @param id train Id.
     * @return number of deleted passengers in the database.
     */
    Integer deleteById(Integer id);

    /**
     * Get count of passengers in the database.
     *
     * @return count of passengers in the database.
     */
    Integer getPassengersCount();
}
