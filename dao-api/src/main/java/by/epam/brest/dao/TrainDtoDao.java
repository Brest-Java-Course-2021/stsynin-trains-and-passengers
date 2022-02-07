package by.epam.brest.dao;

import by.epam.brest.model.dto.TrainDto;

import java.time.LocalDate;
import java.util.List;

/**
 * TrainDto DAO Interface.
 */
public interface TrainDtoDao {

    /**
     * Get trains from а database with passengers counts for a given period of time.
     *
     * @return trains list.
     */
    List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd);
}
