package by.epam.brest.web_app;

import by.epam.brest.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
@Transactional
public class PassengerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private PassengerService passengerService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnPassengerPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passengers")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passengers"))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(1)),
                                hasProperty("passengerName", is("Alfred")),
                                hasProperty("trainId", is(2))
                        )
                )))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(2)),
                                hasProperty("passengerName", is("Bob")),
                                hasProperty("trainId", is(2))
                        )
                )))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(3)),
                                hasProperty("passengerName", is("Chris")),
                                hasProperty("trainId", is(3))
                        )
                )))
        ;
    }
}
