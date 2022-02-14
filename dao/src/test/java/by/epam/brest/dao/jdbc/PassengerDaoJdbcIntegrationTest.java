package by.epam.brest.dao.jdbc;

import by.epam.brest.dao.PassengerDao;
import by.epam.brest.dao.jdbc.exception.ValidationErrorException;
import by.epam.brest.model.Passenger;
import by.epam.brest.testDb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(PassengerDaoJdbc.class)
@PropertySource({"classpath:sql-requests.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PassengerDaoJdbcIntegrationTest {

    @SuppressWarnings("unused")
    @Autowired
    private PassengerDao passengerDao;

    @Test
    void test_findAllPassengers() {
        List<Passenger> passengers = passengerDao.findAll();
        assertNotNull(passengers);
        assertTrue(passengers.size() > 0);
    }

    @Test
    void test_findById() {
        List<Passenger> passengers = passengerDao.findAll();
        Passenger expectedPassenger = passengers.get(0);
        Passenger actualPassenger = passengerDao.findById(expectedPassenger.getPassengerId()).orElse(null);
        assertEquals(expectedPassenger, actualPassenger);
    }

    @Test
    public void test_findPassengerWithNonExistId() {
        assertTrue(passengerDao.findById(999).isEmpty());
    }

    @Test
    public void test_updatePassenger() {
        List<Passenger> passengers = passengerDao.findAll();
        int baseSizeBeforeUpdate = passengers.size();

        Integer renewablePassengerId = passengers.get(0).getPassengerId();
        String newPassengerSName = "newName";
        Integer newPassengerSTrainId = 1;

        Passenger renewablePassenger = new Passenger(newPassengerSName);
        renewablePassenger.setPassengerId(renewablePassengerId);
        renewablePassenger.setTrainId(newPassengerSTrainId);

        Integer resultOfUpdate = passengerDao.updatePassenger(renewablePassenger);
        assertEquals(1, (int) resultOfUpdate, "update failed");

        Passenger actualPassenger = passengerDao.findById(renewablePassenger.getPassengerId()).orElse(null);
        assertEquals(renewablePassenger, actualPassenger);

        assertEquals(baseSizeBeforeUpdate, passengerDao.getPassengersCount());
    }

    @Test
    public void test_updatePassengerWithDuplicatedName() {
        List<Passenger> passengers = passengerDao.findAll();
        Passenger renewablePassenger = passengers.get(0);
        renewablePassenger.setPassengerName(passengers.get(1).getPassengerName());
        assertThrows(ValidationErrorException.class, () ->
                passengerDao.updatePassenger(renewablePassenger)
        );
    }

    @Test
    public void test_updatePassengerWithSameName() {
        Passenger passengerForUpdate = new Passenger("RabbitSameName");
        passengerForUpdate.setTrainId(1);
        Integer testId = passengerDao.createPassenger(passengerForUpdate);

        passengerForUpdate.setTrainId(2);
        Integer resultOfUpdate = passengerDao.updatePassenger(passengerForUpdate);
        assertEquals(1, (int) resultOfUpdate, "update failed");
        assertEquals(2, passengerDao.findById(testId).get().getTrainId(), "wrong save");
    }

    @Test
    public void test_updateNonexistentPassenger() {
        Integer renewablePassengerId = 999;

        Passenger renewablePassenger = new Passenger("newNameForNonexistentPassenger");
        renewablePassenger.setPassengerId(renewablePassengerId);

        Integer resultOfUpdate = passengerDao.updatePassenger(renewablePassenger);
        assertEquals(0, (int) resultOfUpdate, "update nonexistent passenger");
    }

    @Test
    public void test_createNewPassenger() {
        int baseSizeBeforeCreate = passengerDao.getPassengersCount();
        Passenger newPassenger = new Passenger("nameless");
        Integer newPassengerId = passengerDao.createPassenger(newPassenger);
        assertTrue(newPassengerId > 0);
        newPassenger.setPassengerId(newPassengerId);

        assertEquals(baseSizeBeforeCreate + 1, passengerDao.getPassengersCount());
        assertEquals(newPassenger, passengerDao.findById(newPassengerId).orElse(null));
    }

    @Test
    public void test_createPassengerWithExistsName() {
        assertThrows(ValidationErrorException.class, () ->
                passengerDao.createPassenger(new Passenger(passengerDao.findAll().get(0).getPassengerName())));
    }

    @Test
    public void test_deletePassenger() {
        int passengerForDeleteId = passengerDao.createPassenger(new Passenger("nameForDelete"));
        List<Passenger> passengersBeforeDelete = passengerDao.findAll();
        int baseSizeBeforeDelete = passengersBeforeDelete.size();
        assertEquals(1, (int) passengerDao.deletePassenger(passengerForDeleteId));
        assertEquals(baseSizeBeforeDelete - 1, passengerDao.getPassengersCount());
    }

    @Test
    public void test_deleteNonExistsPassenger() {
        Integer resultOfDelete = passengerDao.deletePassenger(999);
        assertEquals(0, (int) resultOfDelete);
    }

    @Test
    void test_getPassengersCount() {
        assertEquals(passengerDao.findAll().size(), passengerDao.getPassengersCount());
    }
}