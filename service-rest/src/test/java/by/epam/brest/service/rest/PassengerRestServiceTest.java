package by.epam.brest.service.rest;

import by.epam.brest.model.ErrorMessage;
import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static by.epam.brest.service.rest.config.TestConfig.PASSENGERS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    PassengerService passengerService;

    private MockRestServiceServer mockServer;

    private final ObjectMapper mapper = new ObjectMapper();

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
                                new Passenger(0, "alf", null),
                                new Passenger(1, "bob", null))))
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
        int id = 1;
        Passenger passenger = new Passenger(id, "alf", 1);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(passenger))
                );

        // when
        Passenger returnedPassenger = passengerService.findById(id);

        // then
        mockServer.verify();
        assertEquals(passenger, returnedPassenger);
    }

    @Test
    public void shouldReturnPassengerNotFoundById() throws Exception {
        LOGGER.debug("shouldReturnPassengerNotFoundById()");

        // given
        int id = 9;
        ErrorMessage errorMessage = new ErrorMessage("test error");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(errorMessage))
                );

        // when
        Exception exception = Assertions.assertThrows(HttpClientErrorException.NotFound.class,
                () -> passengerService.findById(id));

        // then
        mockServer.verify();
        assertEquals("404 Not Found: [{\"message\":\"test error\"}]", exception.getMessage());
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
                        .body(mapper.writeValueAsString("2"))
                );
        // when
        Integer newId = passengerService.createPassenger(passenger);

        // then
        mockServer.verify();
        assertEquals(2, newId);
    }

    @Test
    public void shouldUpdatePassenger() throws Exception {
        LOGGER.debug("shouldUpdatePassenger()");

        // given
        Integer id = 1;
        Passenger passenger = new Passenger(id, "PassengerName", null);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer count = passengerService.updatePassenger(passenger);

        // then
        mockServer.verify();
        assertEquals(1, count);
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
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer count = passengerService.deleteById(id);

        // then
        mockServer.verify();
        assertEquals(1, count);
    }

    @Test
    void shouldGetCountOfPassengers() throws Exception {
        LOGGER.debug("shouldGetCountOfPassengers()");

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_URL + "/count")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("123")));

        // when
        Integer result = passengerService.getPassengersCount();

        // then
        mockServer.verify();
        assertEquals(123, result);
    }
}