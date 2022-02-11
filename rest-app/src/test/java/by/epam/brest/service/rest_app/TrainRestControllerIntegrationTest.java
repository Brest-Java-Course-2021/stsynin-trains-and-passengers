package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_DESTINATION_NAME_LENGTH;
import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_NAME_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = TrainRestController.class)
@Transactional
@ComponentScan(basePackages = "by.epam.brest")
class TrainRestControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestControllerIntegrationTest.class);

    public static final String ENDPOINT_TRAINS = "/trains";
    public static final String ENDPOINT_TRAINS_ID = ENDPOINT_TRAINS + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private TrainRestController trainRestController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
//                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldReturnTrainsList() throws Exception {
        LOGGER.info("shouldReturnTrainsList()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(response);
        assertEquals(4, extractTrainsList(response).size());
    }

    @Test
    public void shouldReturnTrainById() throws Exception {
        LOGGER.info("shouldReturnTrainById()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_TRAINS_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(response);
        Train train = extractTrain(response);
        assertEquals(1, train.getTrainId());
        assertEquals("first", train.getTrainName());
        assertEquals("first direction", train.getTrainDestination());
        assertEquals(LocalDate.of(1970, 1, 1), train.getTrainDepartureDate());
    }

    @Test
    public void shouldReturnTrainNotFoundById() throws Exception {
        LOGGER.info("shouldReturnTrainNotFoundById()");

        // when
        mockMvc.perform(get(ENDPOINT_TRAINS_ID, 9)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("No train with id 9 exists!"));
    }

    @Test
    public void shouldDeleteTrainById() throws Exception {
        LOGGER.info("shouldDeleteTrainById()");

        // when
        mockMvc.perform(delete(ENDPOINT_TRAINS_ID, 4)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNoContent())
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldReturnErrorForDeleteTrainByWrongId() throws Exception {
        LOGGER.info("shouldReturnErrorForDeleteTrainByWrongId()");

        // when
        mockMvc.perform(delete(ENDPOINT_TRAINS_ID, 99)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("No train with id 99 exists!"));
    }

    @Test
    public void shouldReturnErrorForDeleteTrainWithPassengers() throws Exception {
        LOGGER.info("shouldReturnErrorForDeleteTrainWithPassengers()");

        // when
        mockMvc.perform(delete(ENDPOINT_TRAINS_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.message")
                        .value("Delete fail. There are registered passengers. Train id:1"));
    }

    @Test
    public void shouldReturnTrainsCount() throws Exception {
        LOGGER.info("shouldReturnTrainsCount()");

        // when
        mockMvc.perform(get(ENDPOINT_TRAINS + "/count")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    public void shouldReturnIdForCreatedTrain() throws Exception {
        LOGGER.info("shouldReturnIdForCreatedTrain()");

        // given
        Train trainToBeCreated = new Train(null, "bob", "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isCreated())
                .andExpect(content().string("5"));
    }

    @Test
    public void shouldReturnErrorWithDuplicatedNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithDuplicatedNameForCreate()");

        // given
        Train trainToBeCreated = new Train(null, "first", "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Create fail. This name already exists."));

    }

    @Test
    public void shouldReturnErrorWithEmptyNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithEmptyNameForCreate()");

        // given
        Train trainToBeCreated = new Train(null, null, "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Create fail. Train name can't be empty"));
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongNameForCreate()");

        // given
        Train trainToBeCreated = new Train(null, getOverlongName(), "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Create fail. This name is too long."));
    }

    @Test
    public void shouldReturnErrorWithOverlongDestinationNameForCreate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongDestinationNameForCreate()");

        // given
        Train trainToBeCreated = new Train(null, "fourth", getOverlongDestinationName(), LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(post(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Create fail. This name of destination is too long."));
    }

    @Test
    public void shouldReturnUpdatedTrainsCount() throws Exception {
        LOGGER.info("shouldReturnUpdatedTrainsCount()");

        // given
        Train trainToBeCreated = new Train(1, "firstNew", "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_TRAINS)
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
    public void shouldReturnErrorWithDuplicatedNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithDuplicatedNameForUpdate()");

        // given
        Train trainToBeCreated = new Train(1, "second", "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Update fail. This name already exists."));
    }

    @Test
    public void shouldReturnErrorWithEmptyNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithEmptyNameForUpdate()");

        // given
        Train trainToBeCreated = new Train(1, null, "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Update fail. Train name can't be empty"));
    }

    @Test
    public void shouldReturnErrorWithOverlongNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongNameForUpdate()");

        // given
        Train trainToBeCreated = new Train(1, getOverlongName(), "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Update fail. This name is too long."));
    }

    @Test
    public void shouldReturnErrorWithOverlongDestinationNameForUpdate() throws Exception {
        LOGGER.info("shouldReturnErrorWithOverlongDestinationNameForUpdate()");

        // given
        Train trainToBeCreated = new Train(
                1, "first", getOverlongDestinationName(), LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);

        // when
        mockMvc.perform(put(ENDPOINT_TRAINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Update fail. This name of destination is too long."));
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_NAME_LENGTH + 1);
    }

    private String getOverlongDestinationName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_DESTINATION_NAME_LENGTH + 1);
    }

    private Train extractTrain(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(response.getContentAsString(), Train.class);
    }

    private List<Train> extractTrainsList(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(response.getContentAsString(),
                new TypeReference<>() {
                });
    }
}