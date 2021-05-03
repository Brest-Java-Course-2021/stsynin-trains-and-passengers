package by.epam.brest.service.rest_app;

import by.epam.brest.model.ErrorResponse;
import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@RestController
public class PassengerRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestController.class);

    private final PassengerDtoService passengerDtoService;

    private final PassengerService passengerService;

    public PassengerRestController(PassengerDtoService passengerDtoService, PassengerService passengerService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
    }

    /**
     * Endpoint "/passengers". Passengers list.
     *
     * @return ResponseEntity of Passenger list.
     */
    @GetMapping(value = "/passengers", produces = {"application/json"})

    public final ResponseEntity<List<Passenger>> getAll() {
        LOGGER.debug("get passengers list");
        return new ResponseEntity<>(passengerService.findAll(), HttpStatus.OK);
    }

    /**
     * Endpoint "/passengers-dtos". Passengers list.
     *
     * @return ResponseEntity of PassengerDto list.
     */
    @GetMapping(value = "/passengers-dtos", produces = {"application/json"})
    public final ResponseEntity<List<PassengerDto>> getAllDto() {
        LOGGER.debug("get passengers DTO list");
        return new ResponseEntity<>(passengerDtoService.findAllPassengersWithTrainName(), HttpStatus.OK);
    }

    /**
     * Endpoint "/passengers/{id}". Passenger data.
     *
     * @param id passenger id.
     * @return ResponseEntity of passenger data.
     */
    @GetMapping(value = "/passengers/{id}", produces = {"application/json"})
    public final ResponseEntity<Passenger> getById(@PathVariable Integer id) {
        Optional<Passenger> optionalPassenger = passengerService.findById(id);
        if (optionalPassenger.isEmpty()) {
            LOGGER.error("Passenger not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalPassenger.get(), HttpStatus.OK);
    }

    /**
     * Delete passenger information from storage.
     *
     * @param id passenger id.
     * @return number of deleted passengers.
     */
    @DeleteMapping(value = "/passengers/{id}", produces = {"application/json"})
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        Integer deleteResult = passengerService.deletePassenger(id);
        if (deleteResult < 1) {
            LOGGER.error("Delete fail. Passenger not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    /**
     * Endpoint "/passengers/count". Passenger count.
     *
     * @return passengers count.
     */
    @GetMapping(value = "/passengers/count", produces = {"application/json"})
    public final ResponseEntity<Integer> count() {
        return new ResponseEntity<>(passengerService.getPassengersCount(), HttpStatus.OK);
    }

    /**
     * Create new passenger record.
     *
     * @param passenger passenger
     * @return new record id.
     */
    @PostMapping(value = "/passengers", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> create(@RequestBody Passenger passenger) {
        return new ResponseEntity<>(passengerService.createPassenger(passenger), HttpStatus.CREATED);
    }

    /**
     * Update passenger record.
     *
     * @param passenger passenger
     * @return number of updated passengers.
     */
    @PutMapping(value = "/passengers", consumes = {"application/json"}, produces = {"application/json"})
//    public final ResponseEntity<Integer> update(@RequestBody Passenger passenger) {
//        return new ResponseEntity<>(passengerService.updatePassenger(passenger), HttpStatus.OK);
    public final ResponseEntity<ErrorResponse> update(@RequestBody Passenger passenger) {
        Integer result = passengerService.updatePassenger(passenger);
        return new ResponseEntity<>(new ErrorResponse(
                "OK",
                "Passenger id: " + result + " was successfully updated"),
                HttpStatus.OK);
    }
}