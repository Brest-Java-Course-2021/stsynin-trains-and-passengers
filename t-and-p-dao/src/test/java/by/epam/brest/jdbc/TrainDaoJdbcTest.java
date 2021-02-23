package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
    public void test_findAllTrains() {
        List<Train> trains = trainDao.findAll();
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
    }

    @Test
    public void test_findTrainById() {
        Integer testId = 2;
        Optional<Train> trainOptional = trainDao.findById(testId);
        assertTrue(trainOptional.isPresent());
        assertEquals(testId, trainOptional.get().getTrainId());
    }

    @Test
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
    public void test_updateNonexistentTrain() {
        Integer renewableTrainId = 3;
        String newNameOfTrain = "newName";

        Train renewableTrain = new Train(newNameOfTrain);
        renewableTrain.setTrainId(renewableTrainId);

        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
        assertEquals("update nonexistent train", 0, (int) resultOfUpdate);
    }
}