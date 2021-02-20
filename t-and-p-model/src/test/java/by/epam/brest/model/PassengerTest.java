package by.epam.brest.model;

import org.junit.Test;

import static org.junit.Assert.*;

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

        assertEquals("wrong passenger Id", Id, passenger.getPassengerId());
        assertEquals("wrong passenger name", newName, passenger.getPassengerName());
        assertEquals("wrong train", newTrain, passenger.getTrain());
    }

    @Test
    public void test_constructor() {
        String name = "smallFoot";
        Passenger passenger = new Passenger(name);
        assertEquals("wrong train name", name, passenger.getPassengerName());
    }
}