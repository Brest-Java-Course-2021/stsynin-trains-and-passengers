package by.epam.brest.service.rest_app;

import by.epam.brest.model.Acknowledgement;
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
        Acknowledgement acknowledgement = objectMapper.readValue(response.getContentAsString(), Acknowledgement.class);

        assertNotNull(acknowledgement);
        assertEquals("404", acknowledgement.getMessage());
        assertEquals("Resource not found", acknowledgement.getDescriptions());
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
        Acknowledgement acknowledgement = objectMapper.readValue(response.getContentAsString(), Acknowledgement.class);

        assertNotNull(acknowledgement);
        assertEquals("500", acknowledgement.getMessage());
        assertEquals("Internal Server Error", acknowledgement.getDescriptions());
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
        Acknowledgement acknowledgement = objectMapper.readValue(response.getContentAsString(), Acknowledgement.class);

        assertNotNull(acknowledgement);
        assertEquals("501", acknowledgement.getMessage());
        assertEquals("Unknown error", acknowledgement.getDescriptions());
    }
}