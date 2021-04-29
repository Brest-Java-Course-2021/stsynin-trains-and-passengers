package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest
@Transactional
class PassengerRestControllerIntegrationTest {

    public static final String ENDPOINT_PASSENGERS = "/passengers";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvcPassengerService passengerService = new MockMvcPassengerService();

    private MockMvc mockMvc;

    private final PassengerRestController passengerRestController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    PassengerRestControllerIntegrationTest(PassengerRestController passengerRestController) {
        this.passengerRestController = passengerRestController;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(passengerRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldReturnPassengersList() throws Exception {
        List<PassengerDto> passengers = passengerService.findAll();
        assertNotNull(passengers);
        assertTrue(passengers.size() > 0);
    }

    @Test
    public void shouldReturnPassengerById() throws Exception {
        Optional<Passenger> optionalPassenger = passengerService.findById(1);
        assertTrue(optionalPassenger.isPresent());
        assertEquals("Alfred", optionalPassenger.get().getPassengerName());
        assertEquals(2, optionalPassenger.get().getTrainId());
    }

    @Test
    public void shouldReturnPassengerNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS + "/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldDeletePassengerById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_PASSENGERS + "/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Integer errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                Integer.class);
        assertEquals(1, errorResponse);
    }

    @Test
    public void shouldReturnPassengerNotFoundForDeletePassengerByWrongId() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_PASSENGERS + "/999"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldReturnPassengersCount() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS + "/count")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Integer errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                Integer.class);
        assertNotNull(errorResponse);
        assertEquals(6, errorResponse);
    }

    @Test
    public void shouldCreatePassenger() throws Exception {
        Passenger newPassenger = new Passenger("Zombie");
        newPassenger.setTrainId(1);

        Integer newId = passengerService.create(newPassenger);

        assertNotNull(newId);
        Optional<Passenger> optionalPassenger = passengerService.findById(newId);
        assertTrue(optionalPassenger.isPresent());

        Passenger actualPassenger = optionalPassenger.get();
        assertEquals(newId, actualPassenger.getPassengerId());
        assertEquals("Zombie", actualPassenger.getPassengerName());
        assertEquals(1, actualPassenger.getTrainId());

    }

    @Test
    public void shouldReturnErrorWithDuplicatedNameForCreate() throws Exception {
        Passenger newPassenger = new Passenger("Alfred");

        String json = objectMapper.writeValueAsString(newPassenger);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_DUPLICATED_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForCreate() throws Exception {
        Passenger newPassenger = new Passenger();

        String json = objectMapper.writeValueAsString(newPassenger);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_EMPTY_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForCreate() throws Exception {
        Passenger newPassenger = new Passenger(getOverlongName());

        String json = objectMapper.writeValueAsString(newPassenger);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_OVERLONG_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldUpdatePassenger() throws Exception {
        Optional<Passenger> optionalGuineaPig = passengerService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Passenger guineaPig = optionalGuineaPig.get();
        guineaPig.setPassengerName("AlfredNew");

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response);
        Integer UpdateResponse = objectMapper.readValue(response.getContentAsString(), Integer.class);
        assertEquals(1, UpdateResponse);
    }

    @Test
    public void shouldReturnErrorWithDuplicatedNameForUpdate() throws Exception {
        Optional<Passenger> optionalGuineaPig = passengerService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Passenger guineaPig = optionalGuineaPig.get();
        guineaPig.setPassengerName("Bob");

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_DUPLICATED_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForUpdate() throws Exception {
        Optional<Passenger> optionalGuineaPig = passengerService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Passenger guineaPig = optionalGuineaPig.get();
        guineaPig.setPassengerName(null);

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_EMPTY_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForUpdate() throws Exception {
        Optional<Passenger> optionalGuineaPig = passengerService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Passenger guineaPig = optionalGuineaPig.get();
        guineaPig.setPassengerName(getOverlongName());

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_PASSENGERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_OVERLONG_NAME", errorResponse.getMessage());
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_PASSENGER_NAME_LENGTH + 1);
    }

    class MockMvcPassengerService {

        public List<PassengerDto> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(
                    response.getContentAsString(),
                    new TypeReference<>() {
                    });
        }

        public Optional<Passenger> findById(Integer id) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS + "/" + id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            return Optional.of(objectMapper.readValue(
                    response.getContentAsString(),
                    Passenger.class
            ));
        }

        public Integer create(Passenger passenger) throws Exception {
            String json = objectMapper.writeValueAsString(passenger);
            MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}