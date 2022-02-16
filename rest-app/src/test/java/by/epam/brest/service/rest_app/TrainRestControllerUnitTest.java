package by.epam.brest.service.rest_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(TrainRestController.class)
public class TrainRestControllerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestControllerUnitTest.class);

    private static final String URI = "/trains";
    private static final String URI_ID = URI + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @Test
    void shouldReturnTrainsList() throws Exception {
        LOGGER.info("shouldReturnTrainsList()");

        // given
        List<Train> trainsList = Collections.singletonList(
                new Train(1, "name", "aaa", LocalDate.now()));
        when(trainService.findAll()).thenReturn(trainsList);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(servletResponse);
        assertEquals(trainsList, extractTrainsList(servletResponse));
        verify(trainService).findAll();
    }

    @Test
    void shouldReturnTrainById() throws Exception {
        LOGGER.info("shouldReturnTrainById()");

        // given
        Train train = new Train(1, "name", "aaa", LocalDate.now());
        when(trainService.findById(1)).thenReturn(train);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(servletResponse);
        assertEquals(train, extractTrain(servletResponse));
        verify(trainService).findById(1);
    }

    @Test
    void shouldReturnIdForCreatedTrain() throws Exception {
        LOGGER.info("shouldIdForCreatedTrain()");

        // given
        Train trainToBeCreated = new Train(null, "bob", "up", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeCreated);
        when(trainService.createTrain(trainToBeCreated)).thenReturn(4);

        // when
        mockMvc.perform(post(URI, trainToBeCreated)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("4"));

        verify(trainService).createTrain(trainToBeCreated);
    }

    @Test
    void shouldReturnUpdatedTrainsCount() throws Exception {
        LOGGER.info("shouldReturnUpdatedTrainsCount()");

        // given
        Train trainToBeUpdated = new Train(2, "bob", "down", LocalDate.now());
        String json = objectMapper.writeValueAsString(trainToBeUpdated);
        when(trainService.updateTrain(trainToBeUpdated)).thenReturn(1);

        // when
        mockMvc.perform(put(URI, trainToBeUpdated)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1"));

        verify(trainService).updateTrain(trainToBeUpdated);
    }

    @Test
    void shouldReturnDeletedTrainsCount() throws Exception {
        LOGGER.info("shouldReturnDeletedTrainsCount()");

        // given
        when(trainService.deleteById(3)).thenReturn(1);

        // when
        mockMvc.perform(delete(URI_ID, 3)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1"));
        verify(trainService).deleteById(3);
    }

    @Test
    void shouldReturnTrainsCount() throws Exception {
        LOGGER.info("shouldReturnTrainsCount()");

        // given
        when(trainService.getTrainsCount()).thenReturn(3);

        // when
        mockMvc.perform(get(URI + "/count")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("3"));
        verify(trainService).getTrainsCount();
    }

    private Train extractTrain(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                Train.class);
    }

    private List<Train> extractTrainsList(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                new TypeReference<>() {
                });
    }
}
