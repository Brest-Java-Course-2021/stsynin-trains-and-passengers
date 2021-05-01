package by.epam.brest.service.rest;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
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
class TrainDtoRestServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDtoRestServiceTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TrainDtoService trainDtoService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnListOfDtoTrains() throws Exception {
        LOGGER.debug("shouldFindAllTrains()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(
                TRAINS_URL + "?dateStart=2010-10-10&dateEnd=2011-11-11")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createTrainDto(0),
                                createTrainDto(1))))
                );

        // when
        List<TrainDto> trains = trainDtoService.getFilteredByDateTrainListWithPassengersCount(
                LocalDate.of(2010, 10, 10),
                LocalDate.of(2011, 11, 11));

        // then
        mockServer.verify();
        assertNotNull(trains);
        assertEquals(2, trains.size());
    }

    private TrainDto createTrainDto(int index) {
        TrainDto train = new TrainDto();
        train.setTrainId(index);
        train.setTrainName("train_#" + index);
        return train;
    }

}