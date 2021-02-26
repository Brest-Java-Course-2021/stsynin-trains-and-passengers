package by.epam.brest;

import by.epam.brest.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerDao {

    List<Passenger> findAll();

    Optional<Passenger> findById(Integer passengerId);

    Integer createPassenger(Passenger passenger);

    Integer updatePassenger(Passenger passenger);

    Integer deletePassenger(Integer passengerId);
}
