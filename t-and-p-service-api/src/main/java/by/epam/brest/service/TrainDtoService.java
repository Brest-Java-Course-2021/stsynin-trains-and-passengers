package by.epam.brest.service;


import by.epam.brest.model.dto.TrainDto;

import java.util.List;

public interface TrainDtoService {

    /**
     * Get all trains from the database with count of passengers.
     *
     * @return trains list.
     */
    List<TrainDto> findAllWithPassengersCount();
}
