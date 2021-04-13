package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        if (optionalTrain.isPresent()) {
            return new ResponseEntity<>(optionalTrain.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorResponse(
                    "TRAIN_NOT_FOUND",
                    "Train not found id:" + id
            ), HttpStatus.NOT_FOUND);
        }
    }

}
