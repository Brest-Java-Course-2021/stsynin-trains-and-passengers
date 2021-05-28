package by.epam.brest.web_app;

import by.epam.brest.model.Acknowledgement;
import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.PassengerRestService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PassengerController.class)
public class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerRestService passengerService;

    @MockBean
    private PassengerDtoService passengerDtoService;

    @MockBean
    private TrainService trainService;

    @Test
    public void shouldReturnPassengersPage() throws Exception {
        mockMvc.perform(get("/passengers")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passengers"))
        ;
    }

    @Test
    public void shouldOpenNewPassengerPage() throws Exception {
        mockMvc.perform(get("/passenger")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(true)))
        ;
    }

    @Test
    public void shouldAddNewPassenger() throws Exception {

        // when
        when(this.passengerService.createPassenger(any())).thenReturn(new Acknowledgement(
                "OK",
                "Passenger id: 42 was successfully created"));

        mockMvc.perform(post("/passenger")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", "passengerName")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"))
        ;
    }

    @Test
    public void shouldNotAddNewPassengerBecauseEmptyName() throws Exception {
        mockMvc.perform(post("/passenger")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", (String) null)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Passenger name can't be empty")))
        ;
    }

    @Test
    public void shouldNotAddNewPassengerBecauseOverlongName() throws Exception {
        String passengerName = getOverlongName();
        mockMvc.perform(post("/passenger")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", passengerName)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Passenger name " + passengerName + " is too long")))
        ;
    }

    @Test
    public void shouldOpenToEditPassengerPageById() throws Exception {

        // given
        Passenger passenger = new Passenger("PassengerName");
        Integer id = 1;
        passenger.setPassengerId(id);

        // when
        when(passengerService.findById(id)).thenReturn(Optional.of(passenger));

        mockMvc.perform(get("/passenger/" + id)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("passenger", is(passenger)))
        ;
    }

    @Test
    public void shouldRedirectToErrorPageIfPassengerNotFoundById() throws Exception {

        // given
        Integer id = 9;

        // when
        when(passengerService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/passenger/1")
        ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for this passenger.")))
        ;
    }

    @Test
    public void shouldUpdatePassengerAfterEdit() throws Exception {

        // when
        when(this.passengerService.updatePassenger(any())).thenReturn(new Acknowledgement(
                "OK",
                "Passenger id: 42 was successfully updated"));

        mockMvc.perform(post("/passenger/42")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", "passengerName")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"))
        ;
    }

    @Test
    public void shouldNotUpdatePassengerBecauseEmptyName() throws Exception {
        mockMvc.perform(post("/passenger/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", (String) null)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Passenger name can't be empty")))
        ;
    }

    @Test
    public void shouldNotUpdatePassengerBecauseOverlongName() throws Exception {
        String passengerName = getOverlongName();
        mockMvc.perform(post("/passenger/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("passengerName", passengerName)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Passenger name " + passengerName + " is too long")))
        ;
    }

    @Test
    public void shouldDeletePassengerById() throws Exception {
        // given
        Passenger passenger = new Passenger("PassengerName");
        Integer id = 42;
        passenger.setPassengerId(id);

        // when
        when(passengerService.findById(id)).thenReturn(Optional.of(passenger));

        mockMvc.perform(get("/passenger/" + id + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));
    }

    @Test
    public void shouldReturnErrorPageIfTryToDeleteUnknownId() throws Exception {

        // given
        Integer id = 42;

        // when
        when(passengerService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/passenger/" + id + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for delete this passenger.")))
        ;
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_PASSENGER_NAME_LENGTH + 1);
    }
}
