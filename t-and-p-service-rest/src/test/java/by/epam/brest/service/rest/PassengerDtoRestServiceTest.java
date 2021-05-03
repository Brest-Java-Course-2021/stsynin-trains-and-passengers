package by.epam.brest.service.rest;

import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
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

import static by.epam.brest.service.rest.config.TestConfig.PASSENGERS_DTOS_URL;
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
class PassengerDtoRestServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerDtoRestServiceTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PassengerDtoService passengerDtoService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnAllPassengersWithTrainsNames() throws Exception {
        LOGGER.debug("shouldReturnAllPassengersWithTrainsNames()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PASSENGERS_DTOS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createPassengerDto(0),
                                createPassengerDto(1))))
                );

        // when
        List<PassengerDto> trains = passengerDtoService.findAllPassengersWithTrainName();

        // then
        mockServer.verify();
        assertNotNull(trains);
        assertEquals(2, trains.size());
    }

    private PassengerDto createPassengerDto(int index) {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setPassengerId(index);
        passengerDto.setPassengerName("passenger_#" + index);
        return passengerDto;
    }
}