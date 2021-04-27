package by.epam.brest.service.impl;

import by.epam.brest.dao.jdbc.TrainDtoDaoJdbc;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({TrainDtoServiceImpl.class, TrainDtoDaoJdbc.class})
@PropertySource({"classpath:sql-requests.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"by.epam.brest.t-and-p-dao", "com.epam.brest.t-and-p-test-db"})
@Transactional
class TrainDtoServiceImplIntegrationTest {

    @Autowired
    TrainDtoService trainDtoService;

    @Test
    void findAllWithPassengersCount() {
        List<TrainDto> trains = trainDtoService.findAllWithPassengersCount();

        assertNotNull(trains);
        assertTrue(trains.size() > 0);
        assertEquals(1, trains.get(0).getTrainPassengerCount());
        assertEquals(2, trains.get(1).getTrainPassengerCount());
        assertEquals(3, trains.get(2).getTrainPassengerCount());
    }
}