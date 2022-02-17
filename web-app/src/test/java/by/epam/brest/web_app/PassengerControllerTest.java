package by.epam.brest.web_app;

import by.epam.brest.model.ErrorMessage;
import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.PassengerRestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PassengerController.class)
public class PassengerControllerTest {

    public PassengerControllerTest() {
        LOGGER.info("PassengerControllerTest was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerControllerTest.class);

    private final ObjectMapper mapper = new ObjectMapper();

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
        LOGGER.info("shouldReturnPassengersPage()");

        // given
        List<PassengerDto> passengers = Arrays.asList(
                new PassengerDto(0, "first", "down"),
                new PassengerDto(1, "second", "up"));
        when(passengerDtoService.findAllPassengersWithTrainName()).thenReturn(passengers);

        // when
        mockMvc.perform(get("/passengers"))
                .andExpect(status().isOk())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passengers"))
                .andExpect(model().attribute("passengers", is(passengers)));
    }

    @Test
    public void shouldOpenNewPassengerPage() throws Exception {
        LOGGER.info("shouldOpenNewPassengerPage()");

        // when
        mockMvc.perform(get("/passenger"))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(true)));
    }

    @Test
    public void shouldAddNewPassenger() throws Exception {
        LOGGER.info("shouldAddNewPassenger()");

        // given
        Passenger passenger = new Passenger(null, "Alice", 1);
        when(passengerService.createPassenger(passenger)).thenReturn(1024);

        // when
        mockMvc.perform(post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));
        verify(passengerService).createPassenger(passenger);
    }

    @Test
    public void shouldNotAddNewPassengerBecauseEmptyName() throws Exception {
        LOGGER.info("shouldNotAddNewPassengerBecauseEmptyName()");

        // given
        Passenger passenger = new Passenger(null, null, 1);

        // when
        mockMvc.perform(post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        PASSENGER_BLANK_NAME_WARN));
    }

    @Test
    public void shouldNotAddNewPassengerBecauseOverlongName() throws Exception {
        LOGGER.info("shouldNotAddNewPassengerBecauseOverlongName()");

        // given
        String longName = getOverlongName();
        Passenger passenger = new Passenger(null, longName, 1);

        // when
        mockMvc.perform(post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        PASSENGER_OVERLONG_NAME_WARN));
    }

    @Test
    public void shouldReturnPassengerPageToEdit() throws Exception {
        LOGGER.info("shouldReturnPassengerPageToEdit()");

        // given
        Integer id = 1;
        Passenger passenger = new Passenger(1, "Alfred", 1);
        when(passengerService.findById(id)).thenReturn(passenger);

        // when
        mockMvc.perform(get("/passenger/" + id))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("passenger"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("passenger", is(passenger)));
        verify(passengerService).findById(id);
    }

    @Test
    public void shouldRedirectToErrorPageIfPassengerNotFoundById() throws Exception {
        LOGGER.info("shouldRedirectToErrorPageIfPassengerNotFoundById()");

        // given
        Integer id = 9;
        ErrorMessage message = new ErrorMessage("none");
        when(passengerService.findById(id)).thenThrow(getNotFoundErrorException(message));

        // when
        mockMvc.perform(get("/passenger/" + id)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find anything about this.")))
                .andExpect(model().attribute("errorDescription",
                        is("none")));
        verify(passengerService).findById(id);
    }

    @Test
    public void shouldUpdatePassenger() throws Exception {
        LOGGER.info("shouldUpdatePassenger()");

        // given
        Integer id = 1;
        Passenger passenger = new Passenger(id, "Alice", 1);
        when(passengerService.updatePassenger(passenger)).thenReturn(1);

        // when
        mockMvc.perform(post("/passenger/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                // then
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));
        verify(passengerService).updatePassenger(passenger);
    }

    @Test
    public void shouldNotUpdatePassengerBecauseEmptyName() throws Exception {
        LOGGER.info("shouldNotUpdatePassengerBecauseEmptyName()");

        // given
        Integer id = 1;
        Passenger passenger = new Passenger(id, null, 1);

        // when
        mockMvc.perform(post("/passenger/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(PASSENGER_BLANK_NAME_WARN)));
    }

    @Test
    public void shouldNotUpdatePassengerBecauseOverlongName() throws Exception {
        LOGGER.info("shouldNotUpdatePassengerBecauseOverlongName()");

        // given
        Integer id = 1;
        String longName = getOverlongName();
        Passenger passenger = new Passenger(id, longName, 1);

        // when
        mockMvc.perform(post("/passenger/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passenger)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(PASSENGER_OVERLONG_NAME_WARN)));
    }

    @Test
    public void shouldDeletePassengerById() throws Exception {
        LOGGER.info("shouldDeletePassengerById()");

        // given
        Integer id = 42;
        when(passengerService.deleteById(id)).thenReturn(1);

        // when
        mockMvc.perform(get("/passenger/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/passengers"))
                .andExpect(redirectedUrl("/passengers"));
        verify(passengerService).deleteById(id);
    }

    @Test
    public void shouldReturnErrorPageIfTryToDeleteUnknownId() throws Exception {
        LOGGER.info("shouldReturnErrorPageIfTryToDeleteUnknownId()");

        // given
        Integer id = 9;
        ErrorMessage message = new ErrorMessage("help");
        when(passengerService.deleteById(id)).thenThrow(getNotFoundErrorException(message));

        // when
        mockMvc.perform(get("/passenger/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find anything about this.")))
                .andExpect(model().attribute("errorDescription",
                        is("help")));
        verify(passengerService).deleteById(id);
    }

    private HttpClientErrorException getNotFoundErrorException(ErrorMessage message) throws JsonProcessingException {
        return HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "none",
                null,
                mapper.writeValueAsString(message).getBytes(),
                null);
    }

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_PASSENGER_NAME_LENGTH + 1);
    }
}
