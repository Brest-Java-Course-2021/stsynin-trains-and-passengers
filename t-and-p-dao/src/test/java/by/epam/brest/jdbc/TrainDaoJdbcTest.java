package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml", "classpath*:test-dao.xml"})
public class TrainDaoJdbcTest {

    @SuppressWarnings("unused")
    @Autowired
    private TrainDao trainDao;

    @Test
//    @Sql({"/create-test-db.sql", "/init-test-db.sql"})
    public void test_findAllTrains() {
        List<Train> trains = trainDao.findAll();
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
    }

    @Test
    public void test_findTrainById() {
        List<Train> trains = trainDao.findAll();
        Train expectedTrain = trains.get(0);
        Train actualTrain = trainDao.findById(expectedTrain.getTrainId()).orElse(null);
        assertEquals(expectedTrain, actualTrain);
    }

    @Test
    public void test_findTrainWithNonExistId() {
        assertThrows(IllegalArgumentException.class, () ->
                System.out.println(trainDao.findById(999)));
    }

    @Test
    public void test_updateTrain() {
        List<Train> trains = trainDao.findAll();
        int oldDbSize = trains.size();

        Integer renewableTrainId = trains.get(0).getTrainId();
        String newNameOfTrain = "newName";
        String newDestination = "newDirection";
        LocalDate newDepartureDate = LocalDate.now();

        Train renewableTrain = new Train(newNameOfTrain);
        renewableTrain.setTrainId(renewableTrainId);
        renewableTrain.setTrainDestination(newDestination);
        renewableTrain.setTrainDepartureDate(newDepartureDate);

        Integer resultOfUpdate = trainDao.updateTrain(renewableTrain);
        assertEquals(1, (int) resultOfUpdate, "update failed");

        Train actualTrain = trainDao.findById(renewableTrain.getTrainId()).orElse(null);
        assertEquals(renewableTrain, actualTrain);

        List<Train> trainsAfterUpdate = trainDao.findAll();
        assertEquals(oldDbSize, trainsAfterUpdate.size());
    }

    @Test
    public void test_updateTrainWithDuplicatedName() {
        List<Train> trains = trainDao.findAll();
        Train renewableTrain = trains.get(0);
        renewableTrain.setTrainName(trains.get(1).getTrainName());
        assertThrows(IllegalArgumentException.class, () ->
                trainDao.updateTrain(renewableTrain)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
                trainDao.createTrain(new Train(trainDao.findAll().get(0).getTrainName())));
    }

    @Test
    public void test_deleteTrain() {
        List<Train> trainsBeforeDelete = trainDao.findAll();
        int baseSizeBeforeDelete = trainsBeforeDelete.size();
        assertEquals(1, (int) trainDao.deleteTrain(trainsBeforeDelete.get(0).getTrainId()));

        int baseSizeAfterDelete = trainDao.findAll().size();
        assertEquals(baseSizeBeforeDelete - 1, baseSizeAfterDelete);
    }

    @Test
    public void test_deleteNonExistsTrain() {
        Integer testId = 999;
        Integer resultOfDelete = trainDao.deleteTrain(testId);
        assertEquals(0, (int) resultOfDelete);
    }
}