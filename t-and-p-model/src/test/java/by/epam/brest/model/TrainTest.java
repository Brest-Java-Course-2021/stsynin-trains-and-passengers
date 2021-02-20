package by.epam.brest.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TrainTest {

    @Test
    public void test_setters() {
        Train train = new Train();
        Integer newId = 32768;
        String newName = "boeing";
        String newDestination = "hell";
        LocalDate newDepartureDate = LocalDate.now();

        train.setTrainId(newId);
        train.setTrainName(newName);
        train.setTrainDestination(newDestination);
        train.setTrainDepartureDate(newDepartureDate);

        assertEquals("wrong train Id", newId, train.getTrainId());
        assertEquals("wrong train name", newName, train.getTrainName());
        assertEquals("wrong train destination", newDestination, train.getTrainDestination());
        assertEquals("wrong train departure time", newDepartureDate, train.getTrainDepartureDate());
    }

    @Test
    public void test_constructor() {
        String name = "boeing";
        Train train = new Train(name);
        assertEquals("wrong train name", name, train.getTrainName());
    }
}