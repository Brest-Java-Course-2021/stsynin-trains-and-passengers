package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;

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
        if (passenger.getPassengerName() == null) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_EMPTY_NAME",
                    "Create fail. Passenger name can't be empty"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isPassengerNameOverlong(passenger)) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_OVERLONG_NAME",
                    "Create fail. This name is too long : '" + passenger.getPassengerName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (passengerService.isSecondPassengerWithSameNameExists(passenger)) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_DUPLICATED_NAME",
                    "Create fail. This name already exists: '" + passenger.getPassengerName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(passengerService.createPassenger(passenger), HttpStatus.CREATED);
        }
    }

    /**
     * Update passenger record.
     *
     * @param passenger passenger
     * @return number of updated passengers.
     */
    @PutMapping(value = "/passengers/{id}")
    public final ResponseEntity<Integer> update(@RequestBody Passenger passenger) {
        if (passenger.getPassengerName() == null) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_EMPTY_NAME",
                    "Update fail. Passenger name can't be empty"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isPassengerNameOverlong(passenger)) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_OVERLONG_NAME",
                    "Update fail. This name is too long : '" + passenger.getPassengerName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (passengerService.isSecondPassengerWithSameNameExists(passenger)) {
            return new ResponseEntity(new ErrorResponse(
                    "PASSENGER_DUPLICATED_NAME",
                    "Update fail. This name already exists: '" + passenger.getPassengerName() + "'"
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(passengerService.updatePassenger(passenger), HttpStatus.OK);
        }
    }

    private boolean isPassengerNameOverlong(Passenger passenger) {
        return passenger.getPassengerName().length() > MAX_PASSENGER_NAME_LENGTH;
    }
}