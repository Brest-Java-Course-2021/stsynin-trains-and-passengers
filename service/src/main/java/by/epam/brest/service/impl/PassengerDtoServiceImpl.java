package by.epam.brest.service.impl;

import by.epam.brest.dao.PassengerDtoDao;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PassengerDtoServiceImpl implements PassengerDtoService {

    private final PassengerDtoDao passengerDtoDao;

    public PassengerDtoServiceImpl(PassengerDtoDao passengerDtoDao) {
        this.passengerDtoDao = passengerDtoDao;
    }

    @Override
    public List<PassengerDto> findAllPassengersWithTrainName() {
        return passengerDtoDao.findAllPassengersWithTrainName();
    }
}
