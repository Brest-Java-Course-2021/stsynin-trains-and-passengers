package by.epam.brest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassengerTest {

    @Test
    public void test_setters() {
        Passenger passenger = new Passenger();
        Integer Id = 65535;
        String newName = "bigfoot";
        Train newTrain = new Train();

        passenger.setPassengerId(Id);
        passenger.setPassengerName(newName);
        passenger.setTrain(newTrain);

        assertEquals(Id, passenger.getPassengerId(), "wrong passenger Id");
        assertEquals(newName, passenger.getPassengerName(), "wrong passenger name");
        assertEquals(newTrain, passenger.getTrain(), "wrong train");
    }

    @Test
    public void test_constructor() {
        String name = "smallFoot";
        Passenger passenger = new Passenger(name);
        assertEquals(name, passenger.getPassengerName(), "wrong train name");
    }
}