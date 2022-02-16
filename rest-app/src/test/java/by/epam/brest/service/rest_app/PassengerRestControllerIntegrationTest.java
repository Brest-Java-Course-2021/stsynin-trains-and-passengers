package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = PassengerRestController.class)
@Transactional
@ComponentScan(basePackages = "by.epam.brest")
@Sql(scripts = {"classpath:create-test-db.sql", "classpath:init-test-db.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PassengerRestControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerRestControllerIntegrationTest.class);

    public static final String ENDPOINT_PASSENGERS = "/passengers";
    public static final String ENDPOINT_PASSENGERS_ID = ENDPOINT_PASSENGERS + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private PassengerRestController passengerRestController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(passengerRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void shouldReturnPassengersList() throws Exception {
        LOGGER.info("shouldReturnPassengersList()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(response);
        List<Passenger> passengers = extractPassengersList(response);
        assertNotNull(passengers);
        assertEquals(6, passengers.size());
    }

    @Test
    public void shouldReturnPassengerById() throws Exception {
        LOGGER.info("shouldReturnPassengerById()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(response);
        Passenger passenger = extractPassenger(response);
        assertEquals("Alfred", passenger.getPassengerName());
        assertEquals(2, passenger.getTrainId());
    }

    @Test
    public void shouldReturnPassengerNotFoundById() throws Exception {
        LOGGER.info("shouldReturnPassengerNotFoundById()");

        // when
        mockMvc.perform(get(ENDPOINT_PASSENGERS_ID, 9)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("No passenger with id 9 exists!"));
    }

    @Test
    public void shouldCreatePassenger() throws Exception {
        LOGGER.info("shouldCreatePassenger()");

        // given
        Passenger passengerToBeCreated = new Passenger(null, "Zombie", 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("7"));
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithEmptyNameForCreate()");

        // given
        Passenger passengerToBeCreated = new Passenger(null, null, 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Passenger name can't be empty"));
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongNameForCreate()");

        // given
        String longName = getOverlongName();
        Passenger passengerToBeCreated = new Passenger(null, longName, 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("This name is too long"));
    }

    @Test
    public void shouldUpdatePassenger() throws Exception {
        LOGGER.info("shouldUpdatePassenger()");

        // given
        Passenger passengerToBeCreated = new Passenger(1, "AlfredTheSecond", 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithEmptyNameForUpdate()");

        // given
        Passenger passengerToBeCreated = new Passenger(1, null, 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Passenger name can't be empty"));
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongNameForUpdate()");

        // given
        String longName = getOverlongName();
        Passenger passengerToBeCreated = new Passenger(null, longName, 1);
        String json = objectMapper.writeValueAsString(passengerToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("This name is too long"));
    }

    @Test
    public void shouldDeletePassengerById() throws Exception {
        LOGGER.info("shouldDeletePassengerById()");

        // when
        mockMvc.perform(delete(ENDPOINT_PASSENGERS_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNoContent())
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldReturnPassengerNotFoundForDeletePassengerByWrongId() throws Exception {
        LOGGER.info("shouldReturnPassengerNotFoundForDeletePassengerByWrongId()");

        // when
        mockMvc.perform(delete(ENDPOINT_PASSENGERS_ID, 9)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("No passenger with id 9 exists!"));
    }

    @Test
    public void shouldReturnPassengersCount() throws Exception {
        LOGGER.info("shouldReturnPassengersCount()");

        // when
        mockMvc.perform(get(ENDPOINT_PASSENGERS + "/count")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().string("6"));
    }

    private List<Passenger> extractPassengersList(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<>() {
                });
    }

    public Passenger extractPassenger(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(
                response.getContentAsString(),
                Passenger.class);
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_PASSENGER_NAME_LENGTH + 1);
    }
}