package by.epam.brest;

import by.epam.brest.model.Train;

import java.util.List;

public interface TrainDao {

    List<Train> findAll();
}
