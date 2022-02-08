package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDao;
import by.epam.brest.dao.jdbc.exception.ArgumentException;
import by.epam.brest.dao.jdbc.exception.TrainLoadedException;
import by.epam.brest.model.Train;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(TrainDaoJdbc.class)
@PropertySource({"classpath:sql-requests.properties"})
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
        assertEquals(3, trains.size());
    }

    @Test
    public void shouldReturnTrainById() {
        LOGGER.info("shouldReturnTrainById()");

        // when
        Optional<Train> optionalTrain = trainDao.findById(1);

        // then
        assertTrue(optionalTrain.isPresent());
        Train train = optionalTrain.get();
        System.out.println(train);
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
        int oldDbSize = trainDao.count();

        // when
        Integer id = trainDao.createTrain(newTrain);

        // then
        assertEquals(4, id);
        assertEquals(4,trainDao.count());
//        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
//        assertEquals(1, (int) resultOfUpdate, "update failed");
//
//        Train actualTrain = trainDao.findById(renewableTrain.getTrainId()).orElse(null);
//        assertEquals(renewableTrain, actualTrain);
//
//        List<Train> trainsAfterUpdate = trainDao.findAll();
//        assertEquals(oldDbSize, trainsAfterUpdate.size());
    }

    @Test
    public void test_updateTrainWithDuplicatedName() {
        List<Train> trains = trainDao.findAll();
        Train renewableTrain = trains.get(0);
        renewableTrain.setTrainName(trains.get(1).getTrainName());
        assertThrows(ArgumentException.class, () ->
                trainDao.updateTrain(renewableTrain)
        );
    }

    @Test
    public void test_updateTrainWithSameName() {
        Train trainForUpdate = new Train("RabbitSameName");
        trainForUpdate.setTrainDestination("oldDirection");
        Integer testId = trainDao.createTrain(trainForUpdate);

        trainForUpdate.setTrainDestination("newDirection");
        Integer resultOfUpdate = trainDao.updateTrain(trainForUpdate);
        assertEquals(1, (int) resultOfUpdate, "update failed");
        assertEquals("newDirection", trainDao.findById(testId).get().getTrainDestination(), "wrong save");
    }

    @Test
    public void test_updateNonexistentTrain() {
        Integer renewableTrainId = 999;

        Train renewableTrain = new Train("newNameForNonexistentTrain");
        renewableTrain.setTrainId(renewableTrainId);

        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
        assertEquals(0, (int) resultOfUpdate, "update nonexistent train");
    }

    @Test
    public void test_createNewTrain() {
        List<Train> trainsBeforeCreate = trainDao.findAll();
        Train newTrain = new Train("nameless");
        newTrain.setTrainDestination("fakeDestination");
        newTrain.setTrainDepartureDate(LocalDate.of(2011, 11, 11));
        Integer newTrainId = trainDao.createTrain(newTrain);
        assertTrue(newTrainId > 0);
        newTrain.setTrainId(newTrainId);

        List<Train> trainsAfterCreate = trainDao.findAll();
        assertEquals(trainsBeforeCreate.size() + 1, trainsAfterCreate.size());
        assertEquals(newTrain, trainDao.findById(newTrainId).orElse(null));
    }

    @Test
    public void test_createTrainWithExistsName() {
        assertThrows(ArgumentException.class, () ->
                trainDao.createTrain(new Train(trainDao.findAll().get(0).getTrainName())));
    }

    @Test
    public void test_deleteTrain() {
        int trainForDeleteId = trainDao.createTrain(new Train("nameForDelete"));
        List<Train> trainsBeforeDelete = trainDao.findAll();
        int baseSizeBeforeDelete = trainsBeforeDelete.size();
        assertEquals(1, (int) trainDao.deleteTrain(trainForDeleteId));

        assertEquals(baseSizeBeforeDelete - 1, trainDao.count());
    }

    @Test
    public void test_deleteTrainWithPassenger() {
        List<Train> trainsBeforeDelete = trainDao.findAll();
        assertThrows(TrainLoadedException.class, () ->
                trainDao.deleteTrain(trainsBeforeDelete.get(0).getTrainId()));
    }

    @Test
    public void test_deleteNonExistsTrain() {
        Integer testId = 999;
        Integer resultOfDelete = trainDao.deleteTrain(testId);
        assertEquals(0, (int) resultOfDelete);
    }

    @Test
    public void test_getTrainsCount() {
        assertEquals(trainDao.findAll().size(), trainDao.count());
    }
}