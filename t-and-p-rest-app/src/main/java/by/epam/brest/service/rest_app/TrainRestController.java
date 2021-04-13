package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest_app.exception.TrainNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@RestController
public class TrainRestController {

    private final TrainDtoService trainDtoService;

    private final TrainService trainService;

    public TrainRestController(TrainDtoService trainDtoService, TrainService trainService) {
        this.trainDtoService = trainDtoService;
        this.trainService = trainService;
    }

    /**
     * Trains list.
     *
     * @return TrainDto list.
     */
    @GetMapping(value = "/trains")
    public final ResponseEntity<List<TrainDto>> trains() {
        return new ResponseEntity<>(trainDtoService.findAllWithPassengersCount(), HttpStatus.OK);
    }

    /**
     * Train data.
     *
     * @param id train id.
     * @return train data.
     */
    @GetMapping(value = "/trains/{id}")
    public final ResponseEntity<Train> getById(@PathVariable Integer id) {
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isEmpty()) {
            throw new TrainNotFoundException("Train not found for id:" + id);
        }
        return new ResponseEntity<>(optionalTrain.get(), HttpStatus.OK);
    }

    /**
     * Delete train information from storage.
     *
     * @param id train id.
     * @return number of deleted trains.
     */
    @DeleteMapping(value = "/trains/{id}")
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Integer deleteResult = trainService.deleteTrain(id);
        if (deleteResult < 1) {
            throw new TrainNotFoundException("Delete fail. Train not found id:" + id);
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    /**
     * Trains count.
     *
     * @return trains count.
     */
    @GetMapping(value = "/trains/count")
    public final ResponseEntity<Integer> count() {
        return new ResponseEntity<>(trainService.getTrainsCount(), HttpStatus.OK);
    }

    /**
     * Create new train record.
     *
     * @param train train
     * @return new train id.
     */
    @PostMapping(value = "/trains")
    public final ResponseEntity<Integer> create(@RequestBody Train train) {
        return new ResponseEntity<>(trainService.createTrain(train), HttpStatus.CREATED);
    }

    /**
     * Update train record.
     *
     * @param train train
     * @return number of updated trains.
     */
    @PutMapping(value = "/trains/{id}")
    public final ResponseEntity<Integer> update(@RequestBody Train train) {
        return new ResponseEntity<>(trainService.updateTrain(train), HttpStatus.OK);
    }
}
