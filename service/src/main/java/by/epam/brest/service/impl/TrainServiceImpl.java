package by.epam.brest.service.impl;

import by.epam.brest.dao.TrainDao;
import by.epam.brest.dao.jdbc.exception.TrainLoadedException;
import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainServiceImpl implements TrainService {

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
    public Train findById(Integer trainId) {
        return trainDao.findById(trainId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(notFoundForThisIdMessage(trainId)));
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
    public Integer deleteById(Integer trainId) {
        Integer deleteResult;
        try {
            deleteResult = trainDao.deleteTrain(trainId);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new TrainLoadedException("Delete fail. There are registered passengers. Train id:" + trainId);
        }
        if (deleteResult < 1) {
            throw new ResourceNotFoundException(notFoundForThisIdMessage(trainId));
        }
        return deleteResult;
    }

    @Override
    public Integer getTrainsCount() {
        return trainDao.count();
    }

    private String notFoundForThisIdMessage(Integer trainId) {
        return String.format("No train with id %s exists!", trainId);
    }
}
