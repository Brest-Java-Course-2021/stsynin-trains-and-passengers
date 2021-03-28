package by.epam.brest.dao;

import by.epam.brest.model.dto.TrainDto;

import java.util.List;

/**
 * TrainDto DAO Interface.
 */
public interface TrainDtoDao {

    /**
     * Get all trains with passengers count.
     *
     * @return trains list.
     */
    List<TrainDto> findAllWithPassengersCount();
}
