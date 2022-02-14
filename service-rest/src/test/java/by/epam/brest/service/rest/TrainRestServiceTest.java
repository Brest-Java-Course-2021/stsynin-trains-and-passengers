package by.epam.brest.service.rest;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static by.epam.brest.service.rest.config.TestConfig.TRAINS_URL;
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
public class TrainRestServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestServiceTest.class);

    @Autowired
    TrainService trainService;

    @Autowired
    MockRestServiceServer mockServer;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldFindAllTrains() throws Exception {
        LOGGER.debug("shouldFindAllTrains()");

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                new Train(0, "first", "down", LocalDate.now()),
                                new Train(1, "second", "up", LocalDate.now()))))
                );

        // when
        List<Train> trains = trainService.findAll();

        // then
        mockServer.verify();
        assertNotNull(trains);
        assertEquals(2, trains.size());
    }

    @Test
    public void shouldFindTrainById() throws Exception {
        LOGGER.debug("shouldFindTrainById()");

        // given
        int id = 1;
        Train train = new Train(id, "Name", "West", LocalDate.now());
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(train))
                );

        // when
        Train returnedTrain = trainService.findById(id);

        // then
        mockServer.verify();
        assertEquals(train, returnedTrain);
    }

    @Test
    public void shouldCreateTrain() throws Exception {
        LOGGER.debug("shouldCreateTrain()");

        // given
        Train train = new Train(null, "Name", "West", LocalDate.now());
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("2"))
                );
        // when
        Integer id = trainService.createTrain(train);

        // then
        mockServer.verify();
        assertEquals(2, id);
    }

    @Test
    public void shouldUpdateTrain() throws Exception {
        LOGGER.debug("shouldUpdateTrain()");

        // given
        Train train = new Train(2, "Name", "West", LocalDate.now());
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer result = trainService.updateTrain(train);

        // then
        mockServer.verify();
        assertEquals(1, result);
    }

    @Test
    public void shouldDeleteTrain() throws Exception {
        LOGGER.debug("shouldDeleteTrain()");

        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1")));

        // when
        int result = trainService.deleteById(id);

        // then
        mockServer.verify();
        assertEquals(1, result);
    }

    @Test
    void shouldGetCountOfTrains() throws Exception {
        LOGGER.debug("shouldGetCountOfTrains()");

        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL + "/count")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("123")));

        // when
        Integer result = trainService.getTrainsCount();

        // then
        mockServer.verify();
        assertEquals(123, result);
    }
}
