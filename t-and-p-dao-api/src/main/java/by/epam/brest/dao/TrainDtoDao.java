package by.epam.brest.dao;

import by.epam.brest.model.dto.TrainDto;

import java.time.LocalDate;
import java.util.List;

/**
 * TrainDto DAO Interface.
 */
public interface TrainDtoDao {

    /**
     * Get trains from the database with counts of passengers from a period of time.
     *
     * @return trains list.
     */
    List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd);
}
