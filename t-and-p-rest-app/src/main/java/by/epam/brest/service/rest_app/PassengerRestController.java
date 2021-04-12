package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public final ResponseEntity<List<PassengerDto>> getAll() {
        return new ResponseEntity<>(passengerDtoService.findAllPassengersWithTrainName(), HttpStatus.OK);
    }

    /**
     * Endpoint "/passengers/{id}". Passenger data.
     *
     * @param id passenger id.
     * @return ResponseEntity of passenger data.
     */
    @GetMapping(value = "/passengers/{id}")
    public final ResponseEntity<Passenger> getById(@PathVariable Integer id) {
        Optional<Passenger> optionalPassenger = passengerService.findById(id);
        if (optionalPassenger.isPresent()) {
            return new ResponseEntity<>(optionalPassenger.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_NOT_FOUND",
                    "Passenger not found id:" + id
            ), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete passenger information from storage.
     *
     * @param id passenger id.
     * @return number of deleted passengers.
     */
    @DeleteMapping(value = "/passengers/{id}")
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Integer deleteResult = passengerService.deletePassenger(id);
        if (deleteResult > 0) {
            return new ResponseEntity<>(deleteResult, HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_NOT_FOUND",
                    "Delete fail. Passenger not found id:" + id
            ), HttpStatus.NOT_FOUND);
        }
    }
}
