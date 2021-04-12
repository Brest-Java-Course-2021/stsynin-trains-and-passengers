package by.epam.brest.service.rest_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
@Transactional
class PassengerRestControllerIntegrationTest {

    public static final String ENDPOINT_PASSENGERS = "/passengers";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvcPassengerService passengerService = new MockMvcPassengerService();

    private MockMvc mockMvc;

    private final PassengerRestController passengerRestController;

    @Autowired
    PassengerRestControllerIntegrationTest(PassengerRestController passengerRestController) {
        this.passengerRestController = passengerRestController;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(passengerRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
//        .setControllerAdvice(customExceptionHandler)
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
        ErrorResponse errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("PASSENGER_NOT_FOUND", errorResponse.getMessage());
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
    }
}