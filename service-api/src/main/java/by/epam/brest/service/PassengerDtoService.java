package by.epam.brest.service;

import by.epam.brest.model.dto.PassengerDto;

import java.util.List;

/**
 * PassengerDto Service Interface.
 */
public interface PassengerDtoService {

    /**
     * Get all passenger with connected train name.
     *
     * @return passenger list
     */
    List<PassengerDto> findAllPassengersWithTrainName();
}
