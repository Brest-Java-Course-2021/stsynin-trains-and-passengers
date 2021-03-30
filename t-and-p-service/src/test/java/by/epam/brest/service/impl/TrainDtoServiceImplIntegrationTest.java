package by.epam.brest.service.impl;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-service-context.xml"})
@Transactional
class TrainDtoServiceImplIntegrationTest {

    @Autowired
    TrainDtoService trainDtoService;

    @Test
    void findAllWithPassengersCount() {
        List<TrainDto> trains = trainDtoService.findAllWithPassengersCount();
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
//        assertTrue(trains.get(0).getAvgSalary().intValue() > 0);
    }
}