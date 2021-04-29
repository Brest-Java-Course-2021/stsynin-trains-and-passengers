package by.epam.brest.web_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
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
    public void shouldReturnPassengersPage() throws Exception {
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
                                hasProperty("passengerTrainName", is("second"))
                        )
                )))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(2)),
                                hasProperty("passengerName", is("Bob")),
                                hasProperty("passengerTrainName", is("second"))
                        )
                )))
                .andExpect(model().attribute("passengers", hasItem(
                        allOf(
                                hasProperty("passengerId", is(3)),
                                hasProperty("passengerName", is("Chris")),
                                hasProperty("passengerTrainName", is("third"))
                        )
                )))
        ;
    }

    @Test
    public void shouldOpenNewPassengerPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("passenger", isA(Passenger.class)))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(1)),
                                hasProperty("trainName", is("first"))
                        )
                )))
        ;
    }

    @Test
    public void shouldAddNewPassenger() throws Exception {
        Integer countBefore = passengerService.getPassengersCount();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/passenger")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("passengerName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));

        assertEquals(countBefore + 1, passengerService.getPassengersCount());
    }

    @Test
    public void shouldOpenToEditPassengerPageById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("passenger", hasProperty("passengerId", is(1))))
                .andExpect(model().attribute("passenger", hasProperty("passengerName", is("Alfred"))))
                .andExpect(model().attribute("passenger", hasProperty("trainId", is(2))))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(1)),
                                hasProperty("trainName", is("first"))
                        )
                )))
        ;
    }

    @Test
    public void shouldRedirectToErrorPageIfPassengerNotFoundById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for this passenger.")))
        ;
    }

    @Test
    public void shouldUpdatePassengerAfterEdit() throws Exception {
        Integer countBefore = passengerService.getPassengersCount();
//        String testName = RandomStringUtils.randomAlphabetic(DEPARTMENT_NAME_SIZE);
        String testName = "TestNameForPassenger";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/passenger/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("passengerId", "1")
                        .param("passengerName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));

        Optional<Passenger> optionalPassenger = passengerService.findById(1);
        assertTrue(optionalPassenger.isPresent());
        assertEquals(testName, optionalPassenger.get().getPassengerName());
        assertEquals(countBefore, passengerService.getPassengersCount());
    }

    @Test
    public void shouldDeletePassengerById() throws Exception {
        Integer countBefore = passengerService.getPassengersCount();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/3/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("passengerId", "3")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));

        assertEquals(countBefore - 1, passengerService.getPassengersCount());
    }

    @Test
    public void shouldReturnErrorPageIfTryToDeleteUnknownId() throws Exception {
        Integer countBefore = passengerService.getPassengersCount();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/passenger/999/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("passengerId", "999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for delete this passenger.")))
        ;
        assertEquals(countBefore, passengerService.getPassengersCount());
    }
}
