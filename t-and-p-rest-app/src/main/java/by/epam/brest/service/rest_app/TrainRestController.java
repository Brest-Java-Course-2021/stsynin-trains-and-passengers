package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@RestController
public class TrainRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestController.class);

    private final TrainDtoService trainDtoService;

    private final TrainService trainService;

    public TrainRestController(TrainDtoService trainDtoService, TrainService trainService) {
        this.trainDtoService = trainDtoService;
        this.trainService = trainService;
    }

    /**
     * Trains list, filtered by date.
     *
     * @param dateStart start of period of time.
     * @param dateEnd   end of period of time.
     * @return TrainDto list.
     */
    @GetMapping(value = "/trains", produces = {"application/json"})
    public final ResponseEntity<List<TrainDto>> filteredTrains(
            @RequestParam(name = "dateStart", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate dateStart,
            @RequestParam(name = "dateEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate dateEnd) {
        LOGGER.debug("Search trains from: {}, to: {}", dateStart, dateEnd);
        return new ResponseEntity<>(
                trainDtoService.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd),
                HttpStatus.OK);
    }

    /**
     * Train data.
     *
     * @param id train id.
     * @return train data.
     */
    @GetMapping(value = "/trains/{id}", produces = {"application/json"})
    public final ResponseEntity<Train> getById(@PathVariable Integer id) {
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isEmpty()) {
            LOGGER.error("Train not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalTrain.get(), HttpStatus.OK);
    }

    /**
     * Delete train information from storage.
     *
     * @param id train id.
     * @return number of deleted trains.
     */
    @DeleteMapping(value = "/trains/{id}", produces = {"application/json"})
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Integer deleteResult = trainService.deleteTrain(id);
        if (deleteResult < 1) {
            LOGGER.error("Delete fail. Train not found for id: {}", id);
            return new ResponseEntity<>(deleteResult, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    /**
     * Trains count.
     *
     * @return trains count.
     */
    @GetMapping(value = "/trains/count", produces = {"application/json"})
    public final ResponseEntity<Integer> count() {
        return new ResponseEntity<>(trainService.getTrainsCount(), HttpStatus.OK);
    }

    /**
     * Create new train record.
     *
     * @param train train
     * @return new train id.
     */
    @PostMapping(value = "/trains", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> create(@RequestBody Train train) {
        return new ResponseEntity<>(trainService.createTrain(train), HttpStatus.CREATED);
    }

    /**
     * Update train record.
     *
     * @param train train
     * @return number of updated trains.
     */
    @PutMapping(value = "/trains", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> update(@RequestBody Train train) {
        return new ResponseEntity<>(trainService.updateTrain(train), HttpStatus.OK);
    }
}
