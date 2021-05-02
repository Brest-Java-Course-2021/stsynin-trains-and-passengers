package by.epam.brest.service.rest;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@Service
public class PassengerRestService implements PassengerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public PassengerRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get all passengers from the database.
     *
     * @return passengers list.
     */
    @Override
    public List<Passenger> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity<List<Passenger>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return responseEntity.getBody();
    }

    /**
     * Get passenger by Id.
     *
     * @param passengerId passenger Id.
     * @return passenger.
     */
    @Override
    public Optional<Passenger> findById(Integer passengerId) {
        LOGGER.debug("findById({})", passengerId);
        ResponseEntity<Passenger> responseEntity =
                restTemplate.getForEntity(
                        url + "/" + passengerId,
                        Passenger.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Save new passenger record.
     *
     * @param passenger object.
     * @return saved passenger Id.
     */
    @Override
    public Integer createPassenger(Passenger passenger) {
        LOGGER.debug("createPassenger()");
        Object o = restTemplate.postForObject(url, passenger, Object.class);
        LOGGER.debug("raw answer: {}", o);
        if (o instanceof Integer) {
            LOGGER.debug("passenger id: ({}) created", o);
            return (Integer) o;
        }
        LOGGER.error("passenger not created");
        return 0;
    }

    /**
     * Update passenger record in the database.
     *
     * @param passenger object.
     * @return number of updated passengers in the database.
     */
    @Override
    public Integer updatePassenger(Passenger passenger) {
        LOGGER.debug("updatePassenger({})", passenger);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Passenger> entity = new HttpEntity<>(passenger, headers);
        Object o = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Object.class).getBody();
        LOGGER.debug("raw answer: {}", o);
        if (o instanceof Integer) {
            LOGGER.debug("passenger id: ({}) updated", o);
            return (Integer) o;
        }
        LOGGER.error("passenger not updated");
        return 0;
    }

    /**
     * Delete passenger by Id.
     *
     * @param passengerId train Id.
     * @return number of deleted passengers in the database.
     */
    @Override
    public Integer deletePassenger(Integer passengerId) {
        LOGGER.debug("deletePassenger({})", passengerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Passenger> entity = new HttpEntity<>(headers);
        Integer result = restTemplate.exchange(
                url + "/" + passengerId,
                HttpMethod.DELETE,
                entity,
                Integer.class).getBody();
        LOGGER.debug("passenger deleted: ({})", result);
        return result;
    }

    /**
     * Get number of passengers in the database.
     *
     * @return number of passengers in the database.
     */
    @Override
    public Integer getPassengersCount() {
        return null;
    }

    /**
     * Check if this name of passenger is exist in the database.
     *
     * @param passenger
     */
    @Override
    public boolean isSecondPassengerWithSameNameExists(Passenger passenger) {
        return false;
    }
}
