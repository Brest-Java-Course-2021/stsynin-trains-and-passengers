package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(TrainDtoRestController.class)
public class TrainDtoRestControllerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainDtoRestControllerUnitTest.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static final String PERIOD_START = "2020-01-01";
    public static final String PERIOD_END = "2020-02-25";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainDtoService trainDtoService;

    @Test
    void shouldReturnTrainDtoList() throws Exception {
        LOGGER.info("shouldReturnTrainDtoList()");

        // given
        List<TrainDto> trains = Collections.singletonList(
                new TrainDto(42, "Bob", "up", LocalDate.now(), 13));
        when(trainDtoService.getFilteredByDateTrainListWithPassengersCount(
                LocalDate.parse(PERIOD_START),
                LocalDate.parse(PERIOD_END)))
                .thenReturn(trains);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get("/trains-dtos")
                        .param("dateStart", PERIOD_START)
                        .param("dateEnd", PERIOD_END)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(servletResponse);
        assertEquals(trains, extractTrainsList(servletResponse));
        verify(trainDtoService, times(1)).getFilteredByDateTrainListWithPassengersCount(
                LocalDate.parse(PERIOD_START),
                LocalDate.parse(PERIOD_END));
    }

    private List<TrainDto> extractTrainsList(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                new TypeReference<>() {
                });
    }
}
