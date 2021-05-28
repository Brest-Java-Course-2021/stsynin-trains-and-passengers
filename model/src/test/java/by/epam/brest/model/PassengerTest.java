package by.epam.brest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassengerTest {

    @Test
    public void test_setters() {
        Passenger passenger = new Passenger();
        Integer Id = 65535;
        String newName = "bigfoot";
        Integer newTrainId = 32768;

        passenger.setPassengerId(Id);
        passenger.setPassengerName(newName);
        passenger.setTrainId(newTrainId);

        assertEquals(Id, passenger.getPassengerId(), "wrong passenger Id");
        assertEquals(newName, passenger.getPassengerName(), "wrong passenger name");
        assertEquals(newTrainId, passenger.getTrainId(), "wrong train Id");
    }

    @Test
    public void test_constructor() {
        String name = "smallFoot";
        Passenger passenger = new Passenger(name);
        assertEquals(name, passenger.getPassengerName(), "wrong train name");
    }
}