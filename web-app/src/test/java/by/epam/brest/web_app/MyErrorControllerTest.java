package by.epam.brest.web_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest
class MyErrorControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void shouldTranslateErrorMessage() throws Exception {

        // given
        String errorMessageText = "Danger! (32768)";

        // when
        mockMvc.perform(get("/error")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("errorMessage", errorMessageText))

                // then
                .andExpect(model().attribute("errorMessage", is("Danger! (32768)")))
                .andExpect(view().name("error"));
    }

    @Test
    void shouldReturnPageNotFound() throws Exception {

        // when
        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
                            return request;
                        }))

                // then
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage",
                        is("Http Error Code: 404. Resource not found")))
                .andExpect(view().name("error"));
    }

    @Test
    void shouldReturnServerError() throws Exception {

        // when
        mockMvc.perform(get("/error")
                        .with(request -> {
                            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);
                            return request;
                        }))

                // then
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage",
                        is("Http Error Code: 500. Internal Server Error")))
                .andExpect(view().name("error"));
    }
}