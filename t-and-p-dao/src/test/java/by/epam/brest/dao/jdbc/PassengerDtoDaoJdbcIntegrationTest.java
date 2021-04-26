package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.PassengerDtoDao;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@DataJdbcTest
@Import(PassengerDtoDaoJdbc.class)
@PropertySource({"classpath:sql-requests.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PassengerDtoDaoJdbcIntegrationTest {

    @Autowired
    private PassengerDtoDao passengerDtoDao;

    @Test
    void findAllPassengersWithTrainName() {
        List<PassengerDto> passengers = passengerDtoDao.findAllPassengersWithTrainName();
        System.out.println();
        System.out.println(passengers);
        System.out.println();
        //TODO Create right test
    }
}