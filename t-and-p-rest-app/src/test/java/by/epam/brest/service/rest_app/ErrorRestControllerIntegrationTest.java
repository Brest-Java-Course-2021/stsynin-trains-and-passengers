package by.epam.brest.service.rest_app;

import by.epam.brest.service.rest_app.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.RequestDispatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest
class ErrorRestControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mockMvc;

    private final ErrorRestController errorRestController;

    @Autowired
    ErrorRestControllerIntegrationTest(ErrorRestController errorRestController) {
        this.errorRestController = errorRestController;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(errorRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void shouldProcessPageNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
                            return request;
                        })
        )
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("404", errorResponse.getMessage());
        assertEquals("Resource not found", errorResponse.getDescriptions());
    }

    @Test
    void shouldProcessInternalServerError() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);
                            return request;
                        })
        )
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("500", errorResponse.getMessage());
        assertEquals("Internal Server Error", errorResponse.getDescriptions());
    }

    @Test
    void shouldProcessUnknownError() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 100);
                            return request;
                        })
        )
                .andReturn().getResponse();

        assertNotNull(response);
        ErrorResponse errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("501", errorResponse.getMessage());
        assertEquals("Unknown error", errorResponse.getDescriptions());
    }
}