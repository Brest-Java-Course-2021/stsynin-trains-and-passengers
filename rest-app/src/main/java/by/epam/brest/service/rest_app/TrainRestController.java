package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@RestController
@RequestMapping("/trains")
public class TrainRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestController.class);

    @Autowired
    private final TrainService trainService;

    public TrainRestController(TrainService trainService) {
        this.trainService = trainService;
        LOGGER.info("TrainRestController was created");
    }

    /**
     * Trains list.
     *
     * @return Train list.
     */
    @GetMapping(produces = {"application/json"})
    public final ResponseEntity<List<Train>> findAllTrains() {
        LOGGER.info(" IN: findAllTrains() - []");
        LOGGER.debug("Search trains list");
        return new ResponseEntity<>(
                trainService.findAll(),
                HttpStatus.OK);
    }

    /**
     * Train data.
     *
     * @param id train id.
     * @return train data.
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public final ResponseEntity<Train> findTrainById(@PathVariable Integer id) {
        LOGGER.info(" IN: findTrainById() - [{}]", id);
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isEmpty()) {
            LOGGER.error("Train not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.debug("return info for train id: {}", id);
        return new ResponseEntity<>(optionalTrain.get(), HttpStatus.OK);
    }

    /**
     * Delete train information from storage.
     *
     * @param id train id.
     * @return number of deleted trains.
     */
    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public final ResponseEntity<Integer> deleteTrainById(@PathVariable Integer id) {
        LOGGER.info(" IN: deleteTrainById() - [{}]", id);
        Integer deleteResult = trainService.deleteTrain(id);
        if (deleteResult < 1) {
            LOGGER.error("Delete fail. Train not found for id: {}", id);
            return new ResponseEntity<>(deleteResult, HttpStatus.NOT_FOUND);
        }
        LOGGER.debug("train id: {} was deleted", id);
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    /**
     * Trains count.
     *
     * @return trains count.
     */
    @GetMapping(value = "/count", produces = {"application/json"})
    public final ResponseEntity<Integer> trainsCount() {
        LOGGER.info(" IN: trainsCount() - []");
        return new ResponseEntity<>(trainService.getTrainsCount(), HttpStatus.OK);
    }

    /**
     * Create new train record.
     *
     * @param train train
     * @return new train id.
     */
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> createTrain(@RequestBody Train train) {
        LOGGER.info(" IN: createTrain() - [{}]", train);
        return new ResponseEntity<>(trainService.createTrain(train), HttpStatus.CREATED);
    }

    /**
     * Update train record.
     *
     * @param train train
     * @return number of updated trains.
     */
    @PutMapping(consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> updateTrain(@RequestBody Train train) {
        LOGGER.info(" IN: updateTrain() - [{}]", train);
        return new ResponseEntity<>(trainService.updateTrain(train), HttpStatus.OK);
    }
}
