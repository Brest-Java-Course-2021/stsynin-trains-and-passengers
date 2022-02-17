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

/**
 * @author Sergey Tsynin
 */
@Service
public class PassengerRestService implements PassengerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestService.class);

    private final String url;

    private final RestTemplate restTemplate;

    public PassengerRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        LOGGER.info("PassengerRestService was created");
    }

    public List<Passenger> findAll() {
        LOGGER.debug(" IN: findAll() - []");

        ResponseEntity<List<Passenger>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Passenger> passengers = responseEntity.getBody();
        LOGGER.debug("OUT: findAll() - found {} passenger(s)", passengers != null ? passengers.size() : 0);
        return passengers;
    }

    public Passenger findById(Integer id) {
        LOGGER.debug(" IN: findById() - [{}]", id);

        Passenger passenger = restTemplate.getForObject(url + "/" + id, Passenger.class);
        LOGGER.debug("OUT: findById() - [{}]", passenger);
        return passenger;
    }

    public Integer createPassenger(Passenger passenger) {
        LOGGER.debug(" IN: createPassenger() - [{}]", passenger);

        Integer id = restTemplate.postForEntity(
                url,
                passenger,
                Integer.class).getBody();
        LOGGER.debug("OUT: createPassenger() - new passenger id: [{}]", id);
        return id;
//        Acknowledgement response = restTemplate.postForObject(url, passenger, Acknowledgement.class);
//        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
//        if (response.getMessage().equals("OK")) {
//            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
//        } else {
//            LOGGER.error("passenger not created");
//        }
//        return 1;
    }

    public Integer updatePassenger(Passenger passenger) {
        LOGGER.debug(" IN: updatePassenger() - [{}]", passenger);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Passenger> entity = new HttpEntity<>(passenger, headers);
        Integer count = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Integer.class).getBody();
        LOGGER.debug("OUT: updatePassenger() - updated: [{}]", count);
        return count;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        HttpEntity<Passenger> entity = new HttpEntity<>(passenger, headers);
//        Acknowledgement response = restTemplate.exchange(
//                url,
//                HttpMethod.PUT,
//                entity,
//                Acknowledgement.class).getBody();
//        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
//        if (response.getMessage().equals("OK")) {
//            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
//        } else {
//            LOGGER.error("passenger not updated");
//        }
//        return 1;
    }

    public Integer deleteById(Integer id) {
        LOGGER.debug(" IN: deleteById() - [{}]", id);

        restTemplate.exchange(
                url + "/" + id,
                HttpMethod.DELETE,
                null,
                Integer.class);
        LOGGER.debug("OUT: deleteById() - deleted []");
        return 1;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        HttpEntity<Passenger> entity = new HttpEntity<>(headers);
//        Acknowledgement response = restTemplate.exchange(
//                url + "/" + passengerId,
//                HttpMethod.DELETE,
//                entity,
//                Acknowledgement.class).getBody();
//        LOGGER.debug("raw answer: {}", Objects.requireNonNull(response).getMessage());
//        if (response.getMessage().equals("OK")) {
//            LOGGER.debug("answer from rest controller: {}", response.getDescriptions());
//        } else {
//            LOGGER.error("passenger not deleted");
//        }
//        return 1;
    }

    public Integer getPassengersCount() {
        LOGGER.debug(" IN: getPassengersCount() - []");

        Integer count = restTemplate.getForObject(url + "/count", Integer.class);
        LOGGER.debug("OUT: getPassengersCount() - [{}]", count);
        return count;
    }
}
