package by.epam.brest.web_app;

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

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
@Transactional
class TrainControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnTrainsPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/trains")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("trains"))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(1)),
                                hasProperty("trainName", is("first")),
                                hasProperty("trainDestination", is("first direction")),
                                hasProperty("trainDepartureDate", is(LocalDate.of(1970, 1, 1))),
                                hasProperty("trainPassengerCount", is(1))
                        )
                )))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(2)),
                                hasProperty("trainName", is("second")),
                                hasProperty("trainDestination", is("second direction")),
                                hasProperty("trainDepartureDate", is(LocalDate.of(2020, 2, 2))),
                                hasProperty("trainPassengerCount", is(2))
                        )
                )))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(3)),
                                hasProperty("trainName", is("third")),
                                hasProperty("trainDestination", is("third direction")),
                                hasProperty("trainDepartureDate", is(LocalDate.of(2020, 3, 3))),
                                hasProperty("trainPassengerCount", is(3))
                        )
                )))
        ;
    }
}