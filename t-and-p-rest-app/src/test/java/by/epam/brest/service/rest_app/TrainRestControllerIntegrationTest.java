package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;
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
class TrainRestControllerIntegrationTest {

    public static final String ENDPOINT_TRAINS = "/trains";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final MockMvcTrainService trainService = new MockMvcTrainService();

    private MockMvc mockMvc;

    private final TrainRestController trainRestController;

    @Autowired
    TrainRestControllerIntegrationTest(TrainRestController trainRestController) {
        this.trainRestController = trainRestController;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldReturnTrainsList() throws Exception {
        List<TrainDto> trains = trainService.findAll();
        System.out.println(trains);
        assertNotNull(trains);
        assertTrue(trains.size() > 0);
    }

    @Test
    public void shouldReturnTrainById() throws Exception {
        Optional<Train> optionalTrain = trainService.findById(1);
        assertTrue(optionalTrain.isPresent());
        System.out.println(optionalTrain.get());
        assertEquals(1, optionalTrain.get().getTrainId());
        assertEquals("first", optionalTrain.get().getTrainName());
        assertEquals("first direction", optionalTrain.get().getTrainDestination());
        LocalDate testDate = LocalDate.of(1970, 1, 1);
        assertEquals(testDate, optionalTrain.get().getTrainDepartureDate());
    }

    @Test
    public void shouldReturnTrainNotFoundById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS + "/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_NOT_FOUND", errorResponse.getMessage());
    }

    class MockMvcTrainService {

        public List<TrainDto> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(
                    response.getContentAsString(),
                    new TypeReference<>() {
                    });
        }

        public Optional<Train> findById(Integer id) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS + "/" + id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            return Optional.of(objectMapper.readValue(
                    response.getContentAsString(),
                    Train.class
            ));
        }
    }
}