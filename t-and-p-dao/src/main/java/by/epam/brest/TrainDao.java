package by.epam.brest;

import by.epam.brest.model.Train;

import java.util.List;
import java.util.Optional;

public interface TrainDao {

    List<Train> findAll();

    Optional<Train> findById(Integer trainId);
}
