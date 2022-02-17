package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = TrainDtoRestController.class)
@Transactional
@ComponentScan(basePackages = "by.epam.brest")
class TrainDtoRestControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDtoRestControllerIntegrationTest.class);

    public static final String URI = "/trains-dtos";
    public static final String PERIOD_START = "2020-01-01";
    public static final String PERIOD_END = "2020-02-25";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mockMvc;

    @Autowired
    private TrainDtoRestController trainDtoRestController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainDtoRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
//                .alwaysDo(print())
                .build();
    }

    @Test
    public void shouldReturnTrainBetweenTwoDates() throws Exception {
        LOGGER.info("shouldReturnTrainBetweenTwoDates()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_START)
                        .param("dateEnd", PERIOD_END)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(1, trains.size());
        assertEquals(2, trains.get(0).getTrainId());
    }

    @Test
    public void shouldReturnTrainsFromStartDate() throws Exception {
        LOGGER.info("shouldReturnTrainsFromStartDate()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(3, trains.size());
        assertFalse(getAllTrainsIds(trains).contains(1));
    }

    @Test
    public void shouldReturnTrainsBeforeEndDate() throws Exception {
        LOGGER.info("shouldReturnTrainsBeforeEndDate()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateEnd", PERIOD_END)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(2, trains.size());
        assertFalse(getAllTrainsIds(trains).contains(3));
    }

    @Test
    public void shouldReturnErrorWithWrongFiltersOrder() throws Exception {
        LOGGER.info("shouldReturnErrorWithWrongFiltersOrder()");

        // when
        mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_END)
                        .param("dateEnd", PERIOD_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Wrong date order for filtering"));
    }

    private List<Integer> getAllTrainsIds(List<TrainDto> trains) {
        List<Integer> result = new ArrayList<>();
        for (TrainDto train : trains) {
            result.add(train.getTrainId());
        }
        return result;
    }

    private List<TrainDto> getRemappedTrains(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(response.getContentAsString(),
                new TypeReference<>() {
                });
    }
}