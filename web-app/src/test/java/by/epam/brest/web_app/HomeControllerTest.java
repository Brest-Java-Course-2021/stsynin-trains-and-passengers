package by.epam.brest.web_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRedirectTrainsPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:trains"))
                .andExpect(redirectedUrl("trains"))
        ;
    }
}