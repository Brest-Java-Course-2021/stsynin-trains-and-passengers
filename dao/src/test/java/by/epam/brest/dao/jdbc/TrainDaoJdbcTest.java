package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDao;
import by.epam.brest.model.Train;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(TrainDaoJdbc.class)
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainDaoJdbcTest {

    @SuppressWarnings("unused")
    @Autowired
    private TrainDao trainDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDaoJdbcTest.class);

    @Test
    public void shouldReturnTrainsList() {
        LOGGER.info("shouldReturnTrainsList()");

        // when
        List<Train> trains = trainDao.findAll();

        // then
        assertNotNull(trains);
        assertEquals(4, trains.size());
    }

    @Test
    public void shouldReturnTrainById() {
        LOGGER.info("shouldReturnTrainById()");

        // when
        Optional<Train> optionalTrain = trainDao.findById(1);

        // then
        assertTrue(optionalTrain.isPresent());
        Train train = optionalTrain.get();
        assertEquals(1, train.getTrainId());
        assertEquals("first", train.getTrainName());
        assertEquals("first direction", train.getTrainDestination());
        assertEquals(LocalDate.of(1970, 1, 1), train.getTrainDepartureDate());
    }

    @Test
    public void shouldCreateNewTrain() {
        LOGGER.info("shouldCreateNewTrain()");

        // given
        Train newTrain = new Train(0, "newName", "newDirection", LocalDate.now());

        // when
        Integer id = trainDao.createTrain(newTrain);

        // then
        assertEquals(5, id);
        assertEquals(5, trainDao.count());
    }

    @Test
    public void shouldReturnUpdatedTrainsCount() {
        LOGGER.info("shouldReturnUpdatedTrainsCount()");

        // given
        Train trainToBeCreated = new Train(1, "firstNew", "up", LocalDate.now());

        // when
        Integer count = trainDao.updateTrain(trainToBeCreated);

        // then
        assertEquals(1, count);
        assertEquals(4, trainDao.count());
    }

    @Test
    public void shouldDeleteTrainById() {
        LOGGER.info("shouldDeleteTrainById()");

        // when
        Integer count = trainDao.deleteTrain(4);

        // then
        assertEquals(1, count);
        assertEquals(3, trainDao.count());
    }

    @Test
    public void shouldReturnTrainsCount() {
        LOGGER.info("shouldReturnTrainsCount()");

        // when
        Integer count = trainDao.count();

        // then
        assertEquals(4, count);
    }
}