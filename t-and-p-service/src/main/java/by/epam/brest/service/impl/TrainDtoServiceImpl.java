package by.epam.brest.service.impl;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * TrainDto Service.
 */
@Service
@Transactional
public class TrainDtoServiceImpl implements TrainDtoService {

    private final TrainDtoDao trainDtoDao;

    public TrainDtoServiceImpl(TrainDtoDao trainDtoDao) {
        this.trainDtoDao = trainDtoDao;
    }

    /**
     * Get trains from the database with counts of passengers from a period of time.
     *
     * @param dateStart start of period.
     * @param dateEnd   end of period.
     * @return trains list.
     */
    @Override
    public List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd) {
        return trainDtoDao.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd);
    }
}
