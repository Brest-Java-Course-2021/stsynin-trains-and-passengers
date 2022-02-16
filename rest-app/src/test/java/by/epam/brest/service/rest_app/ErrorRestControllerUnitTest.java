package by.epam.brest.service.rest_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.RequestDispatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = ErrorRestController.class)
class ErrorRestControllerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorRestControllerUnitTest.class);

    private MockMvc mockMvc;

    private final ErrorRestController errorRestController;

    @Autowired
    ErrorRestControllerUnitTest(ErrorRestController errorRestController) {
        this.errorRestController = errorRestController;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(errorRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
//                .alwaysDo(print())
                .build();
    }

    @Test
    void shouldProcessPageNotFound() throws Exception {
        LOGGER.info("shouldProcessPageNotFound()");

        // when
        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
                            return request;
                        }))

                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Resource [null] was not found"));
    }

    @Test
    void shouldProcessUnknownError() throws Exception {
        LOGGER.info("shouldProcessUnknownError()");

        // when
        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 100);
                            return request;
                        }))

                // then
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("Unknown error while [null] request"));
    }
}