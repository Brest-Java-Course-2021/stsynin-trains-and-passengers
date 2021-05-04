package by.epam.brest.service.rest;

import by.epam.brest.model.Acknowledgement;
import by.epam.brest.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@Service
public class PassengerRestService {

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
     * @return Acknowledgement with saved passenger Id.
     */
    public Acknowledgement createPassenger(Passenger passenger) {
        LOGGER.debug("createPassenger()");
        Acknowledgement response = restTemplate.postForObject(url, passenger, Acknowledgement.class);
        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
        if (response.getMessage().equals("OK")) {
            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
        } else {
            LOGGER.error("passenger not created");
        }
        return response;
    }

    /**
     * Update passenger record in the database.
     *
     * @param passenger object.
     * @return Acknowledgement.
     */
    public Acknowledgement updatePassenger(Passenger passenger) {
        LOGGER.debug("updatePassenger({})", passenger);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Passenger> entity = new HttpEntity<>(passenger, headers);
        Acknowledgement response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Acknowledgement.class).getBody();
        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
        if (response.getMessage().equals("OK")) {
            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
        } else {
            LOGGER.error("passenger not updated");
        }
        return response;
    }

    /**
     * Delete passenger by Id.
     *
     * @param passengerId train Id.
     * @return Acknowledgement.
     */
    public Acknowledgement deletePassenger(Integer passengerId) {
        LOGGER.debug("deletePassenger({})", passengerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Passenger> entity = new HttpEntity<>(headers);
        Acknowledgement response = restTemplate.exchange(
                url + "/" + passengerId,
                HttpMethod.DELETE,
                entity,
                Acknowledgement.class).getBody();
        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
        if (response.getMessage().equals("OK")) {
            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
        } else {
            LOGGER.error("passenger not deleted");
        }
        return response;
    }

    /**
     * Get number of passengers in the database.
     *
     * @return number of passengers in the database.
     */
    public Integer getPassengersCount() {
        return null;
    }

    /**
     * Check if this name of passenger is exist in the database.
     *
     * @param passenger
     */
    public boolean isSecondPassengerWithSameNameExists(Passenger passenger) {
        return false;
    }
}
