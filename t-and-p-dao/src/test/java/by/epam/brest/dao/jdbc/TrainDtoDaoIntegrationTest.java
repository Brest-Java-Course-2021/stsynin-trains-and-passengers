package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.model.dto.TrainDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author sergey
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
public class TrainDtoDaoIntegrationTest {

    @Autowired
    private TrainDtoDao trainDtoDao;

    @Test
    void test1() {
        List<TrainDto> trains = trainDtoDao.findAllWithPassengersCount();
        System.out.println();
        System.out.println(trains);
        System.out.println();
        //TODO Create right test
    }
}
