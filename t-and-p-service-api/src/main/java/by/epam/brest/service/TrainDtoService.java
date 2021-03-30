package by.epam.brest.service;


import by.epam.brest.model.dto.TrainDto;

import java.util.List;

public interface TrainDtoService {

    List<TrainDto> findAllWithPassengersCount();
}
