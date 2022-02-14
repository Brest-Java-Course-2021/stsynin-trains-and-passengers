package by.epam.brest.service.rest;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
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

import static by.epam.brest.service.rest.config.TestConfig.TRAINS_URL;
import static org.junit.jupiter.api.Assertions.*;
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
    RestTemplate restTemplate;

    @Autowired
    TrainService trainService;

    private MockRestServiceServer mockServer;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllTrains() throws Exception {

        LOGGER.debug("shouldFindAllTrains()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createTrain(0),
                                createTrain(1))))
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
        Integer id = 1;
        Train train = createTrain(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(train))
                );

        // when
        Optional<Train> optionalTrain = trainService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalTrain.isPresent());
        assertEquals(id, optionalTrain.get().getTrainId());
        assertEquals(train.getTrainName(), optionalTrain.get().getTrainName());
    }

    @Test
    public void shouldCreateTrain() throws Exception {

        LOGGER.debug("shouldCreateTrain()");
        // given
        Train train = new Train("TrainName");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = trainService.createTrain(train);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldUpdateTrain() throws Exception {

        LOGGER.debug("shouldUpdateTrain()");
        // given
        Integer id = 1;
        Train train = new Train("TrainName");
        train.setTrainId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TRAINS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(train))
                );

        // when
        int result = trainService.updateTrain(train);
        Optional<Train> updatedTrainOptional = trainService.findById(id);

        // then
        mockServer.verify();
        assertEquals(1, result);

        assertTrue(updatedTrainOptional.isPresent());
        assertEquals(id, updatedTrainOptional.get().getTrainId());
        assertEquals(train.getTrainName(), updatedTrainOptional.get().getTrainName());
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

    private Train createTrain(int index) {
        Train train = new Train();
        train.setTrainId(index);
        train.setTrainName("train_#" + index);
        return train;
    }
}
