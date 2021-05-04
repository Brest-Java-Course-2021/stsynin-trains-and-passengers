package by.epam.brest.service.rest;

import by.epam.brest.model.Acknowledgement;
import by.epam.brest.model.Passenger;
import by.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static by.epam.brest.service.rest.config.TestConfig.PASSENGERS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class PassengerRestServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestServiceTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PassengerRestService passengerService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllPassengers() throws Exception {

        LOGGER.debug("shouldFindAllPassengers()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createPassenger(0),
                                createPassenger(1))))
                );

        // when
        List<Passenger> passengers = passengerService.findAll();

        // then
        mockServer.verify();
        assertNotNull(passengers);
        assertEquals(2, passengers.size());
    }

    @Test
    public void shouldFindPassengerById() throws Exception {

        LOGGER.debug("shouldFindPassengerById()");
        // given
        Integer id = 1;
        Passenger passenger = createPassenger(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(passenger))
                );

        // when
        Optional<Passenger> optionalPassenger = passengerService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalPassenger.isPresent());
        assertEquals(id, optionalPassenger.get().getPassengerId());
        assertEquals(passenger.getPassengerName(), optionalPassenger.get().getPassengerName());
    }

    @Test
    public void shouldCreatePassenger() throws Exception {

        LOGGER.debug("shouldCreatePassenger()");
        // given
        Passenger passenger = new Passenger("PassengerName");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new Acknowledgement(
                                "OK",
                                "Passenger id: 11 was successfully created")))
                );
        // when
        Acknowledgement result = passengerService.createPassenger(passenger);

        // then
        mockServer.verify();
        assertNotNull(result);
        assertEquals("OK", result.getMessage());
    }

    @Test
    public void shouldUpdatePassenger() throws Exception {

        LOGGER.debug("shouldUpdatePassenger()");
        // given
        Integer id = 1;
        Passenger passenger = new Passenger("PassengerName");
        passenger.setPassengerId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new Acknowledgement(
                                "OK",
                                "Passenger id: " + id + " was successfully updated")))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(passenger))
                );

        // when
        Acknowledgement result = passengerService.updatePassenger(passenger);
        Optional<Passenger> updatedPassengerOptional = passengerService.findById(id);

        // then
        mockServer.verify();
        assertNotNull(result);
        assertEquals("OK", result.getMessage());

        assertTrue(updatedPassengerOptional.isPresent());
        assertEquals(passenger, updatedPassengerOptional.get());
    }

    @Test
    public void shouldDeletePassenger() throws Exception {

        LOGGER.debug("shouldDeletePassenger()");
        // given
        Integer id = 44;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new Acknowledgement(
                                "OK",
                                "Passenger id: " + id + " was successfully deleted",
                                id)))
                );
        // when
        Acknowledgement result = passengerService.deletePassenger(id);

        // then
        mockServer.verify();
        assertNotNull(result);
        assertEquals("OK", result.getMessage());
    }

    private Passenger createPassenger(int index) {
        Passenger passenger = new Passenger();
        passenger.setPassengerId(index);
        passenger.setPassengerName("passenger_#" + index);
        return passenger;
    }
}