package by.epam.brest.dao;

import by.epam.brest.model.dto.PassengerDto;

import java.util.List;

/**
 * PassengerDto DAO Interface.
 */
public interface PassengerDtoDao {

    /**
     * Get all passenger with connected train name.
     *
     * @return passenger list
     */
    List<PassengerDto> findAllPassengersWithTrainName();
}
