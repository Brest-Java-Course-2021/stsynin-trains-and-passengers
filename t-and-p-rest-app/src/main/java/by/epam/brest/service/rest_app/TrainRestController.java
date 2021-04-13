package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_DESTINATION_NAME_LENGTH;
import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_NAME_LENGTH;

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
        if (optionalTrain.isPresent()) {
            return new ResponseEntity<>(optionalTrain.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_NOT_FOUND",
                    "Train not found id:" + id
            ), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete train information from storage.
     *
     * @param id train id.
     * @return number of deleted trains.
     */
    @DeleteMapping(value = "/trains/{id}")
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        if (trainService.isTrainLoaded(id)) {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_LOADED",
                    "Delete fail. There are registered passengers. Train id:" + id
            ), HttpStatus.LOCKED);
        }
        Integer deleteResult = trainService.deleteTrain(id);
        if (deleteResult > 0) {
            return new ResponseEntity<>(deleteResult, HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_NOT_FOUND",
                    "Delete fail. Train not found id:" + id
            ), HttpStatus.NOT_FOUND);
        }
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
        if (isTrainNameOverlong(train)) {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_OVERLONG_NAME",
                    "Create fail. This name is too long : '" + train.getTrainName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isTrainDestinationNameOverlong(train)) {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_OVERLONG_DESTINATION_NAME",
                    "Create fail. This name of destination is too long : '" + train.getTrainDestination() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (trainService.isSecondTrainWithSameNameExists(train)) {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_DUPLICATED_NAME",
                    "Create fail. This name already exists: '" + train.getTrainName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(trainService.createTrain(train), HttpStatus.CREATED);
        }
    }

    private boolean isTrainDestinationNameOverlong(Train train) {
        return train.getTrainDestination() != null && train.getTrainDestination().length() > MAX_TRAIN_DESTINATION_NAME_LENGTH;
    }

    private boolean isTrainNameOverlong(Train train) {
        return train.getTrainName().length() > MAX_TRAIN_NAME_LENGTH;
    }
}
