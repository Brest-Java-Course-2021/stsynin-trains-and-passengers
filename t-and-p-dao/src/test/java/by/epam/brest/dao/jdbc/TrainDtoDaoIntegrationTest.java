package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.TrainDtoDao;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author sergey
 */
@DataJdbcTest
@Import(TrainDtoDaoJdbc.class)
@PropertySource({"classpath:sql-requests.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainDtoDaoIntegrationTest {

    @Autowired
    private TrainDtoDao trainDtoDao;

//    @Test
//    void test1() {
//        List<TrainDto> trains = trainDtoDao.findAllWithPassengersCount();
//        System.out.println();
//        System.out.println(trains);
//        System.out.println();
//        //TODO Create right test
//    }
}
