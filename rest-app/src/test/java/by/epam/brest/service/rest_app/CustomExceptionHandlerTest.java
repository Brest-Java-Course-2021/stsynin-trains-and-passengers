package by.epam.brest.service.rest_app;

import by.epam.brest.service.TrainService;
import by.epam.brest.service.exception.ResourceLockedException;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(TrainRestController.class)
public class CustomExceptionHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandlerTest.class);

    private static final String URI = "/trains";
    private static final String URI_ID = URI + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @Test
    void shouldReturnNotFoundError() throws Exception {
        LOGGER.info("shouldReturnNotFoundError()");

        // given
        when(trainService.findById(9)).thenThrow(new ResourceNotFoundException("not found"));

        // when
        mockMvc.perform(get(URI_ID, 9)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
        verify(trainService).findById(9);
    }

    @Test
    void shouldReturnTrainLoadedError() throws Exception {
        LOGGER.info("shouldReturnTrainLoadedError()");

        // given
        when(trainService.deleteById(1)).thenThrow(new ResourceLockedException("TrainLoadedError"));

        // when
        mockMvc.perform(delete(URI_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("TrainLoadedError"));
        verify(trainService).deleteById(1);
    }

    @Test
    void shouldReturnInternalServerError() throws Exception {
        LOGGER.info("shouldReturnInternalServerError()");

        // given
        when(trainService.getTrainsCount()).thenThrow(new RuntimeException("test useless string"));

        // when
        mockMvc.perform(get(URI + "/count")
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Happened unknown error!"));
        verify(trainService).getTrainsCount();
    }
}
