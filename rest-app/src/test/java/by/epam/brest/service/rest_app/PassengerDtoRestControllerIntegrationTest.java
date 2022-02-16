package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = PassengerDtoRestController.class)
@Transactional
@ComponentScan(basePackages = "by.epam.brest")
@Sql(scripts = {"classpath:create-test-db.sql", "classpath:init-test-db.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PassengerDtoRestControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerDtoRestControllerIntegrationTest.class);

    public static final String ENDPOINT_PASSENGERS = "/passengers-dtos";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private PassengerDtoRestController passengerDtoRestController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(passengerDtoRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
//                .alwaysDo(print())
                .build();
    }

    @Test
    public void shouldReturnPassengersListWithTrainsNames() throws Exception {
        LOGGER.info("shouldReturnPassengersListWithTrainsNames()");

        // when
        MockHttpServletResponse response = mockMvc.perform(get(ENDPOINT_PASSENGERS)
                        .accept(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(response);
        List<PassengerDto> passengers = extractPassengerDtosList(response);
        assertNotNull(passengers);
        assertEquals(6, passengers.size());
    }

    private List<PassengerDto> extractPassengerDtosList(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<>() {
                });
    }
}
