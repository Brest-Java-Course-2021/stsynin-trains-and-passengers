package by.epam.brest.service.impl;

import by.epam.brest.dao.TrainDao;
import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainServiceImpl implements TrainService {

//    Logger logger = LoggerFactory.getLogger(PassengerServiceImpl.class);

    private final TrainDao trainDao;

    @Autowired
    public TrainServiceImpl(TrainDao trainDao) {
        this.trainDao = trainDao;
    }

    @Override
    public List<Train> findAll() {
        return trainDao.findAll();
    }

    @Override
    public Optional<Train> findById(Integer trainId) {
        return trainDao.findById(trainId);
    }

    @Override
    public Integer createTrain(Train train) {
        return trainDao.createTrain(train);
    }

    @Override
    public Integer updateTrain(Train train) {
        return trainDao.updateTrain(train);
    }

    @Override
    public Integer deleteTrain(Integer trainId) {
        return trainDao.deleteTrain(trainId);
    }

    @Override
    public Integer getTrainsCount() {
        return trainDao.getTrainsCount();
    }

    @Override
    public boolean isSecondTrainWithSameNameExists(Train train) {
        return trainDao.isSecondTrainWithSameNameExists(train);
    }

    @Override
    public boolean isTrainLoaded(Integer trainId) {
        return trainDao.isTrainLoaded(trainId);
    }
}
