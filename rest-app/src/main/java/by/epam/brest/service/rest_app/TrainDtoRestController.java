package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
public class TrainDtoRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDtoRestController.class);

    @Autowired
    private final TrainDtoService trainDtoService;

    public TrainDtoRestController(TrainDtoService trainDtoService) {
        this.trainDtoService = trainDtoService;
        LOGGER.info("TrainDtoRestController was created");
    }

    /**
     * Trains list, filtered by date.
     *
     * @param dateStart start of period of time.
     * @param dateEnd   end of period of time.
     * @return TrainDto list.
     */
    @GetMapping(value = "/trains-dtos", produces = {"application/json"})
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
}
