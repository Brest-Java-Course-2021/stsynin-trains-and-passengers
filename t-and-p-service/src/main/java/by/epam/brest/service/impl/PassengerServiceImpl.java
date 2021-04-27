package by.epam.brest.service.impl;

import by.epam.brest.dao.PassengerDao;
import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Passenger> findById(Integer passengerId) {
        return passengerDao.findById(passengerId);
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
        return passengerDao.deletePassenger(passengerId);
    }

    @Override
    public Integer getPassengersCount() {
        return passengerDao.getPassengersCount();
    }

    @Override
    public boolean isSecondPassengerWithSameNameExists(Passenger passenger) {
        return passengerDao.isSecondPassengerWithSameNameExists(passenger);
    }
}
