package by.epam.brest.service.rest_app;

import by.epam.brest.model.Acknowledgement;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest
@Transactional
class TrainDtoRestControllerIntegrationTest {

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
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void shouldReturnTrainBetweenTwoDates() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_START)
                        .param("dateEnd", PERIOD_END)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(1, trains.size());
        assertEquals(2, trains.get(0).getTrainId());
    }

    @Test
    public void shouldReturnTrainsFromStartDate() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(2, trains.size());
        assertFalse(getAllTrainsIds(trains).contains(1));
    }

    @Test
    public void shouldReturnTrainsBeforeEndDate() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateEnd", PERIOD_END)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);

        List<TrainDto> trains = getRemappedTrains(response);
        assertEquals(2, trains.size());
        assertFalse(getAllTrainsIds(trains).contains(3));
    }

    @Test
    public void shouldReturnErrorWithWrongFiltersOrder() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(URI)
                        .param("dateStart", PERIOD_END)
                        .param("dateEnd", PERIOD_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
        Acknowledgement acknowledgement = getRemappedError(response);
        assertNotNull(acknowledgement);
        assertEquals("TRAINS_WRONG_FILTER", acknowledgement.getMessage());
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

    private Acknowledgement getRemappedError(MockHttpServletResponse response) throws Exception {
        return getRemappedObject(response, Acknowledgement.class);
    }

    private <T> T getRemappedObject(MockHttpServletResponse response, Class<T> valueType) throws Exception {
        return objectMapper.readValue(response.getContentAsString(), valueType);
    }
}