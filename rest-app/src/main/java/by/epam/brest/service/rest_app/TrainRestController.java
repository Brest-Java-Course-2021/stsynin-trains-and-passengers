package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Get all trains list.
     *
     * @return trains list.
     */
    @GetMapping(produces = {"application/json"})
    public final List<Train> findAllTrains() {
        LOGGER.info(" IN: findAllTrains() - []");
        List<Train> trains = trainService.findAll();
        LOGGER.info("OUT: findAllTrains() - found {} train(s)", trains.size());
        return trains;
    }

    /**
     * Get train by id.
     *
     * @param id train id.
     * @return train.
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public final Train findTrainById(@PathVariable Integer id) {
        LOGGER.info(" IN: findTrainById() - [{}]", id);
        Train train = trainService.findById(id);
        LOGGER.info("OUT: findTrainById() - [{}]", train);
        return train;
    }

    /**
     * Save the new train.
     *
     * @param train object
     * @return new train id.
     */
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public final Integer createTrain(@RequestBody Train train) {
        LOGGER.info(" IN: createTrain() - [{}]", train);
        Integer id = trainService.createTrain(train);
        LOGGER.info("OUT: createTrain() - [{}]", id);
        return id;
    }

    /**
     * Update train record in the database.
     *
     * @param train object
     * @return number of updated trains in the database.
     */
    @PutMapping(consumes = {"application/json"}, produces = {"application/json"})
    public final Integer updateTrain(@RequestBody Train train) {
        LOGGER.info(" IN: updateTrain() - [{}]", train);
        Integer count = trainService.updateTrain(train);
        LOGGER.info("OUT: updateTrain() - [{}]", count);
        return count;
    }

    /**
     * Delete train by id.
     *
     * @param id train id.
     * @return number of deleted trains in the database.
     */
    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final Integer deleteTrainById(@PathVariable Integer id) {
        LOGGER.info(" IN: deleteTrainById() - [{}]", id);
        Integer count = trainService.deleteById(id);
        LOGGER.info("OUT: deleteTrainById() - [{}]", count);
        return count;
    }

    /**
     * Get count of trains in the database.
     *
     * @return count of trains in the database.
     */
    @GetMapping(value = "/count", produces = {"application/json"})
    public final Integer trainsCount() {
        LOGGER.info(" IN: trainsCount() - []");
        Integer count = trainService.getTrainsCount();
        LOGGER.info("OUT: trainsCount() - [{}]", count);
        return count;
    }
}
