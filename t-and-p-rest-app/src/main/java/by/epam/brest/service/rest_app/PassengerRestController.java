package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
public class PassengerRestController {

    private final PassengerDtoService passengerDtoService;

    private final PassengerService passengerService;

    private final TrainService trainService;

    public PassengerRestController(PassengerDtoService passengerDtoService, PassengerService passengerService, TrainService trainService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
        this.trainService = trainService;
    }

    /**
     * Endpoint "/passengers". Passengers list.
     *
     * @return ResponseEntity of PassengerDto list.
     */
    @GetMapping(value = "/passengers")
    public final ResponseEntity<List<PassengerDto>> passengers() {
        return new ResponseEntity<>(passengerDtoService.findAllPassengersWithTrainName(), HttpStatus.OK);
    }
}
