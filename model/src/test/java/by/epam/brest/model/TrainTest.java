package by.epam.brest.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(newId, train.getTrainId(), "wrong train Id");
        assertEquals(newName, train.getTrainName(), "wrong train name");
        assertEquals(newDestination, train.getTrainDestination(), "wrong train destination");
        assertEquals(newDepartureDate, train.getTrainDepartureDate(), "wrong train departure time");
    }

    @Test
    public void test_constructor() {
        String name = "boeing";
        Train train = new Train(name);
        assertEquals(name, train.getTrainName(), "wrong train name");
    }
}