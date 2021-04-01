package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.PassengerDtoDao;
import by.epam.brest.model.dto.PassengerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
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