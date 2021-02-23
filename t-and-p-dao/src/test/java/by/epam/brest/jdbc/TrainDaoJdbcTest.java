package by.epam.brest.jdbc;

import by.epam.brest.TrainDao;
import by.epam.brest.model.Train;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
public class TrainDaoJdbcTest {

    @Autowired
    private TrainDao trainDao;

    @Test
    public void test_findAll() {
        List<Train> trains = trainDao.findAll();
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
    }

    @Test
    public void test_findById() {
        Integer testId = 2;
        Optional<Train> trainOptional = trainDao.findById(testId);
        assertTrue(trainOptional.isPresent());
        assertEquals(testId, trainOptional.get().getTrainId());
    }

}