package by.epam.brest.service.impl;

import by.epam.brest.dao.PassengerDao;
import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private final PassengerDao passengerDao;

    @Autowired
    public PassengerServiceImpl(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    @Override
    public List<Passenger> findAll() {
        return passengerDao.findAll();
    }

    @Override
    public Passenger findById(Integer passengerId) {
        return passengerDao.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundForThisIdMessage(passengerId)));
    }

    @Override
    public Integer createPassenger(Passenger passenger) {
        return passengerDao.createPassenger(passenger);
    }

    @Override
    public Integer updatePassenger(Passenger passenger) {
        return passengerDao.updatePassenger(passenger);
    }

    @Override
    public Integer deletePassenger(Integer passengerId) {
        Integer deleteResult = passengerDao.deletePassenger(passengerId);
        if (deleteResult < 1) {
            throw new ResourceNotFoundException(notFoundForThisIdMessage(passengerId));
        }
        return deleteResult;
    }

    @Override
    public Integer getPassengersCount() {
        return passengerDao.getPassengersCount();
    }

    @Override
    public boolean isSecondPassengerWithSameNameExists(Passenger passenger) {
        return passengerDao.isSecondPassengerWithSameNameExists(passenger);
    }

    private String notFoundForThisIdMessage(Integer trainId) {
        return String.format("No passenger with id %s exists!", trainId);
    }
}
