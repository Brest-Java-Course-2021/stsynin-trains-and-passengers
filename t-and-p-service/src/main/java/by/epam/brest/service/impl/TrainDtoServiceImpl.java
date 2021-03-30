package by.epam.brest.service.impl;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainDtoServiceImpl implements TrainDtoService {

    private final TrainDtoDao trainDtoDao;

    public TrainDtoServiceImpl(TrainDtoDao trainDtoDao) {
        this.trainDtoDao = trainDtoDao;
    }

    @Override
    public List<TrainDto> findAllWithPassengersCount() {
        return trainDtoDao.findAllWithPassengersCount();
    }
}
