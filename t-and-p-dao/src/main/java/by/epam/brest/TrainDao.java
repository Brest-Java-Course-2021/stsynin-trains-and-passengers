package by.epam.brest;

import by.epam.brest.model.Train;

import java.util.List;
import java.util.Optional;

public interface TrainDao {

    List<Train> findAll();

    Optional<Train> findById(Integer trainId);

    Integer createTrain(Train train);

    Integer updateTrain(Train train);

    Integer deleteTrain(Integer trainId);

    Integer getTrainsCount();
}
