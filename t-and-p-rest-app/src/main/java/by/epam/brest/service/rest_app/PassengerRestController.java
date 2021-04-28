package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.rest_app.exception.PassengerNotFoundException;
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

    private final PassengerDtoService passengerDtoService;

    private final PassengerService passengerService;

    public PassengerRestController(PassengerDtoService passengerDtoService, PassengerService passengerService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
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
        if (optionalPassenger.isEmpty()) {
            throw new PassengerNotFoundException("Passenger not found for id:" + id);
        }
        return new ResponseEntity<>(optionalPassenger.get(), HttpStatus.OK);
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
        if (deleteResult < 1) {
            throw new PassengerNotFoundException("Delete fail. Passenger not found id:" + id);
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    /**
     * Endpoint "/passengers/count". Passenger count.
     *
     * @return passengers count.
     */
    @GetMapping(value = "/passengers/count")
    public final ResponseEntity<Integer> count() {
        return new ResponseEntity<>(passengerService.getPassengersCount(), HttpStatus.OK);
    }

    /**
     * Create new passenger record.
     *
     * @param passenger passenger
     * @return new record id.
     */
    @PostMapping(value = "/passengers")
    public final ResponseEntity<Integer> create(@RequestBody Passenger passenger) {
        return new ResponseEntity<>(passengerService.createPassenger(passenger), HttpStatus.CREATED);
    }

    /**
     * Update passenger record.
     *
     * @param passenger passenger
     * @return number of updated passengers.
     */
    @PutMapping(value = "/passengers")
    public final ResponseEntity<Integer> update(@RequestBody Passenger passenger) {
        return new ResponseEntity<>(passengerService.updatePassenger(passenger), HttpStatus.OK);
    }
}