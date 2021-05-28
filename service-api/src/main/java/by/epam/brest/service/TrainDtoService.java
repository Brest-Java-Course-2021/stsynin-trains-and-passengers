package by.epam.brest.service;


import by.epam.brest.model.dto.TrainDto;

import java.time.LocalDate;
import java.util.List;

/**
 * TrainDto Service Interface.
 */
public interface TrainDtoService {

    /**
     * Get trains from the database with counts of passengers from a period of time.
     *
     * @param dateStart start of period.
     * @param dateEnd   end of period.
     * @return trains list.
     */
    List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd);
}
