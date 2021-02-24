package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
public class TrainDaoJdbcTest {

    @Autowired
    private TrainDao trainDao;

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_findAllTrains() {
        List<Train> trains = trainDao.findAll();
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_findTrainById() {
        Integer testId = 2;
        Optional<Train> trainOptional = trainDao.findById(testId);
        assertTrue(trainOptional.isPresent());
        assertEquals(testId, trainOptional.get().getTrainId());
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_updateTrain() {
        Integer renewableTrainId = 2;
        String newNameOfTrain = "newName";
        String newDestination = "newDirection";
        // LocalDate newDepartureDate= LocalDate.now();

        Train renewableTrain = new Train(newNameOfTrain);
        renewableTrain.setTrainId(renewableTrainId);
        renewableTrain.setTrainDestination(newDestination);
        // renewableTrain.setTrainDepartureDate(newDepartureDate);

        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
        assertEquals("update failed", 1, (int) resultOfUpdate);

        Optional<Train> updatedTrain = trainDao.findById(renewableTrainId);
        assertTrue(updatedTrain.isPresent());
        assertEquals(newNameOfTrain, updatedTrain.get().getTrainName());
        // assertEquals(newDestination, updatedTrain.get().getTrainDestination());
        // assertEquals(newDepartureDate,updatedTrain.get().getTrainDepartureDate());
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_updateNonexistentTrain() {
        Integer renewableTrainId = 3;
        String newNameOfTrain = "newName";

        Train renewableTrain = new Train(newNameOfTrain);
        renewableTrain.setTrainId(renewableTrainId);

        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
        assertEquals("update nonexistent train", 0, (int) resultOfUpdate);
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_createNewTrain() {
        String newTrainName = "nameless";
        Train newTrain = new Train(newTrainName);
        Integer newTrainId = trainDao.createTrain(newTrain);
        assertEquals(3, (int) newTrainId);

        Optional<Train> trainOptional = trainDao.findById(newTrainId);
        assertTrue(trainOptional.isPresent());
        assertEquals(newTrainName, trainOptional.get().getTrainName());
//        assertEquals(newTrainName, trainOptional.get().getTrainDestination());
//        assertEquals(newTrainName, trainOptional.get().getTrainDepartureDate());
    }

    @Test(expected = IllegalArgumentException.class)
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_createTrainWithExistsName() {
        String newTrainName = "first";
        Train newTrain = new Train(newTrainName);
        trainDao.createTrain(newTrain);
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_deleteTrain() {
        Integer testId = 1;
        Integer resultOfDelete = trainDao.deleteTrain(testId);
        assertEquals(1, (int) resultOfDelete);
    }

    @Test
    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_deleteNonExistsTrain() {
        Integer testId = 5;
        Integer resultOfDelete = trainDao.deleteTrain(testId);
        assertEquals(0, (int) resultOfDelete);
    }
}