package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.rest_app.exception.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_DESTINATION_NAME_LENGTH;
import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_NAME_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void shouldDeleteTrainById() throws Exception {
        Train train = new Train("zombie");
        Integer freeTrainId = trainService.create(train);
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_TRAINS + "/" + freeTrainId))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Integer errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                Integer.class);
        assertEquals(1, errorResponse);
    }

    @Test
    public void shouldReturnErrorForDeleteTrainByWrongId() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_TRAINS + "/999"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_NOT_FOUND", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorForDeleteTrainWithPassengers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_TRAINS + "/1"))
                .andExpect(status().isLocked())
                .andReturn().getResponse();
        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_LOADED", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnTrainsCount() throws Exception {
        int expectedCount = trainService.findAll().size();

        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS + "/count")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Integer errorResponse = objectMapper.readValue(
                response.getContentAsString(),
                Integer.class);
        assertNotNull(errorResponse);
        assertEquals(expectedCount, errorResponse);
    }

    @Test
    public void shouldCreateTrain() throws Exception {
        Train newTrain = new Train("Zombie");
        newTrain.setTrainDestination("west");
        newTrain.setTrainDepartureDate(LocalDate.now());

        Integer newId = trainService.create(newTrain);
        newTrain.setTrainId(newId);

        assertNotNull(newId);
        Optional<Train> optionalTrain = trainService.findById(newId);
        assertTrue(optionalTrain.isPresent());

        Train actualTrain = optionalTrain.get();
        assertEquals(newTrain, actualTrain);

    }

    @Test
    public void shouldReturnErrorWithDuplicatedNameForCreate() throws Exception {
        Train newTrain = new Train("first");

        String json = objectMapper.writeValueAsString(newTrain);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_TRAINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_DUPLICATED_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForCreate() throws Exception {
        Train newTrain = new Train();

        String json = objectMapper.writeValueAsString(newTrain);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_TRAINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_EMPTY_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForCreate() throws Exception {
        Train newTrain = new Train(getOverlongName());

        String json = objectMapper.writeValueAsString(newTrain);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_TRAINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_OVERLONG_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongDestinationNameForCreate() throws Exception {
        Train newTrain = new Train("zombie");
        newTrain.setTrainDestination(getOverlongDestinationName());

        String json = objectMapper.writeValueAsString(newTrain);
        MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_TRAINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_OVERLONG_DESTINATION_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldUpdateTrain() throws Exception {
        Optional<Train> optionalGuineaPig = trainService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Train guineaPig = optionalGuineaPig.get();
        guineaPig.setTrainName("firstNew");

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_TRAINS + "/1")
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
        Optional<Train> optionalGuineaPig = trainService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Train guineaPig = optionalGuineaPig.get();
        guineaPig.setTrainName("second");

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_TRAINS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_DUPLICATED_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForUpdate() throws Exception {
        Optional<Train> optionalGuineaPig = trainService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Train guineaPig = optionalGuineaPig.get();
        guineaPig.setTrainName(null);

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_TRAINS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_EMPTY_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForUpdate() throws Exception {
        Optional<Train> optionalGuineaPig = trainService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Train guineaPig = optionalGuineaPig.get();
        guineaPig.setTrainName(getOverlongName());

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_TRAINS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_OVERLONG_NAME", errorResponse.getMessage());
    }

    @Test
    public void shouldReturnErrorWithOverlongDestinationNameForUpdate() throws Exception {
        Optional<Train> optionalGuineaPig = trainService.findById(1);
        assertTrue(optionalGuineaPig.isPresent());

        Train guineaPig = optionalGuineaPig.get();
        guineaPig.setTrainDestination(getOverlongDestinationName());

        String json = objectMapper.writeValueAsString(guineaPig);
        MockHttpServletResponse response = mockMvc.perform(put(ENDPOINT_TRAINS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("TRAIN_OVERLONG_DESTINATION_NAME", errorResponse.getMessage());
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_NAME_LENGTH + 1);
    }

    private String getOverlongDestinationName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_DESTINATION_NAME_LENGTH + 1);
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

        public Integer create(Train train) throws Exception {
            String json = objectMapper.writeValueAsString(train);
            MockHttpServletResponse response = mockMvc.perform(post(ENDPOINT_TRAINS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}