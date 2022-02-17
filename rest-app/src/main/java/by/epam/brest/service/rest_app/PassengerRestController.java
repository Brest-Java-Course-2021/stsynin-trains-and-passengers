package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
@RequestMapping("/passengers")
public class PassengerRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestController.class);

    private final PassengerService passengerService;

    public PassengerRestController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Passengers list.
     *
     * @return Passenger list.
     */
    @GetMapping(produces = {"application/json"})
    public final List<Passenger> getAll() {
        LOGGER.info(" IN: getAll() - []");
        List<Passenger> passengers = passengerService.findAll();
        LOGGER.info("OUT: getAll() - [found {} passenger(s)]", passengers.size());
        return passengers;
    }

    /**
     * Passenger data.
     *
     * @param id passenger id.
     * @return Passenger data.
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public final Passenger getById(@PathVariable Integer id) {
        LOGGER.info(" IN: getById() - [{}]", id);
        Passenger passenger = passengerService.findById(id);
        LOGGER.info("OUT: getById() - [{}]", passenger);
        return passenger;
    }

    /**
     * Create new passenger record.
     *
     * @param passenger passenger
     * @return new passenger id.
     */
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public final Integer createPassenger(@Valid @RequestBody Passenger passenger) {
        LOGGER.info(" IN: createPassenger() - [{}]", passenger);
        Integer id = passengerService.createPassenger(passenger);
        LOGGER.info("OUT: createPassenger() - [new passenger id:{}]", id);
        return id;
    }

    /**
     * Update passenger record.
     *
     * @param passenger passenger
     * @return number of updated passengers.
     */
    @PutMapping(consumes = {"application/json"}, produces = {"application/json"})
    public final Integer updatePassenger(@Valid @RequestBody Passenger passenger) {
        LOGGER.info(" IN: updatePassenger() - [{}]", passenger);
        Integer result = passengerService.updatePassenger(passenger);
        LOGGER.info("OUT: updatePassenger() - [{}]", result);
        return result;
    }

    /**
     * Delete passenger information by id.
     *
     * @param id passenger id.
     * @return number of deleted passengers in the database.
     */
    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final Integer deletePassengerById(@PathVariable Integer id) {
        LOGGER.info(" IN: deletePassengerById - [{}]", id);
        Integer deleteResult = passengerService.deleteById(id);
        LOGGER.info("OUT: deletePassengerById - [passengers deleted:{}]", deleteResult);
        return deleteResult;
    }

    /**
     * Passenger count.
     *
     * @return passengers count.
     */
    @GetMapping(value = "/count", produces = {"application/json"})
    public final Integer count() {
        LOGGER.info(" IN: count() - []");
        Integer count = passengerService.getPassengersCount();
        LOGGER.info("OUT: count() - [{}]", count);
        return count;
    }
}