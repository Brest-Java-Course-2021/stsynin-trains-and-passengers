package by.epam.brest.dao.jdbc;

import by.epam.brest.PassengerDao;
import by.epam.brest.model.Passenger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
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
        assertThrows(IllegalArgumentException.class, () ->
                System.out.println(passengerDao.findById(999)));
    }

    @Test
    public void test_updatePassenger() {
        List<Passenger> passengers = passengerDao.findAll();
        int baseSizeBeforeUpdate = passengers.size();

        Integer renewablePassengerId = passengers.get(0).getPassengerId();
        System.out.println(passengers.get(0));
        String newPassengerSName = "newName";
        Integer newPassengerSTrainId = 1;

        Passenger renewablePassenger = new Passenger(newPassengerSName);
        renewablePassenger.setPassengerId(renewablePassengerId);
        renewablePassenger.setTrainId(newPassengerSTrainId);

        Integer resultOfUpdate = passengerDao.updatePassenger(renewablePassenger);
        assertEquals(1, (int) resultOfUpdate, "update failed");

        Passenger actualPassenger = passengerDao.findById(renewablePassenger.getPassengerId()).orElse(null);
        System.out.println(actualPassenger);
        assertEquals(renewablePassenger, actualPassenger);

        assertEquals(baseSizeBeforeUpdate, passengerDao.getPassengersCount());
    }

    @Test
    public void test_updatePassengerWithDuplicatedName() {
        List<Passenger> passengers = passengerDao.findAll();
        Passenger renewablePassenger = passengers.get(0);
        renewablePassenger.setPassengerName(passengers.get(1).getPassengerName());
        assertThrows(IllegalArgumentException.class, () ->
                passengerDao.updatePassenger(renewablePassenger)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
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