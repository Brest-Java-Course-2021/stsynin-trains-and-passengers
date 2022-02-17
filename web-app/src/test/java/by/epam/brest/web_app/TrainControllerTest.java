package by.epam.brest.web_app;

import by.epam.brest.model.ErrorMessage;
import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
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
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static by.epam.brest.model.constants.TrainConstants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TrainController.class)
class TrainControllerTest {

    public TrainControllerTest() {
        LOGGER.info("TrainControllerTest was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainControllerTest.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @MockBean
    private TrainDtoService trainDtoService;

    @Test
    public void shouldReturnTrainsPageAfterSearch() throws Exception {
        LOGGER.info("shouldReturnTrainsPageAfterSearch()");

        // given
        LocalDate dateStart = LocalDate.of(2010, 10, 10);
        LocalDate dateEnd = LocalDate.of(2011, 11, 11);
        List<TrainDto> trainDtoList = Arrays.asList(
                new TrainDto(0, "first", "down", LocalDate.now(), 1),
                new TrainDto(1, "second", "up", LocalDate.now(), 2));
        when(trainDtoService.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd))
                .thenReturn(trainDtoList);

        // when
        mockMvc.perform(get("/trains")
                        .param("dateStart", String.valueOf(dateStart))
                        .param("dateEnd", String.valueOf(dateEnd)))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("dateStart", is(dateStart)))
                .andExpect(model().attribute("dateEnd", is(dateEnd)))
                .andExpect(model().attribute("trains", is(trainDtoList)))
                .andExpect(view().name("trains"));
        verify(trainDtoService).getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd);
    }

    @Test
    public void shouldReturnErrorPageWithWrongFiltersOrder() throws Exception {
        LOGGER.info("shouldReturnErrorPageWithWrongFiltersOrder()");

        // when
        mockMvc.perform(get("/trains?dateStart=2011-11-11&dateEnd=2010-10-10"))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is("The start date {2011-11-11} is later than the end date {2010-10-10}.")))
                .andExpect(view().name("error"));
    }

    @Test
    public void shouldReturnTrainPageToEdit() throws Exception {
        LOGGER.info("shouldReturnTrainPageToEdit()");

        // given
        Integer id = 1;
        Train train = new Train(id, "bob", "up", LocalDate.now());
        when(trainService.findById(id)).thenReturn(train);

        // when
        mockMvc.perform(get("/train/" + id))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("train", is(train)));
        verify(trainService).findById(id);
    }

    @Test
    public void shouldRedirectToErrorPageIfTrainNotFoundById() throws Exception {
        LOGGER.info("shouldRedirectToErrorPageIfTrainNotFoundById()");

        // given
        Integer id = 9;
        ErrorMessage message = new ErrorMessage("help");
        when(trainService.findById(id)).thenThrow(getNotFoundErrorException(message));

        // when
        mockMvc.perform(get("/train/" + id))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find anything about this.")))
                .andExpect(model().attribute("errorDescription",
                        is("help")));
        verify(trainService).findById(id);
    }

    @Test
    public void shouldOpenNewTrainPage() throws Exception {
        LOGGER.info("shouldOpenNewTrainPage()");

        // when
        mockMvc.perform(get("/train"))

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(true)))
        ;
    }

    @Test
    public void shouldAddNewTrain() throws Exception {
        LOGGER.info("shouldAddNewTrain()");

        // given
        Train train = new Train(null, "new", "nowhere", LocalDate.now());
        when(trainService.createTrain(train)).thenReturn(69);

        // when
        mockMvc.perform(post("/train")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));
        verify(trainService).createTrain(train);
    }

    @Test
    public void shouldNotAddNewTrainBecauseEmptyName() throws Exception {
        LOGGER.info("shouldNotAddNewTrainBecauseEmptyName()");

        // given
        Train train = new Train(null, null, "nowhere", LocalDate.now());

        // when
        mockMvc.perform(post("/train")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_BLANK_NAME_WARN)));
    }

    @Test
    public void shouldNotAddNewTrainBecauseOverlongName() throws Exception {
        LOGGER.info("shouldNotAddNewTrainBecauseOverlongName()");

        // given
        String trainName = getOverlongName();
        Train train = new Train(null, trainName, "nowhere", LocalDate.now());

        // when
        mockMvc.perform(post("/train")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_OVERLONG_NAME_WARN)));
    }

    @Test
    public void shouldNotAddNewTrainBecauseOverlongDestinationName() throws Exception {
        LOGGER.info("shouldNotAddNewTrainBecauseOverlongDestinationName()");

        // given
        String trainDestination = getOverlongDestinationName();
        Train train = new Train(null, "new", trainDestination, LocalDate.now());

        // when
        mockMvc.perform(post("/train")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_OVERLONG_DESTINATION_NAME_WARN)));
    }

    @Test
    public void shouldUpdateTrain() throws Exception {
        LOGGER.info("shouldUpdateTrain()");

        // given
        Train train = new Train(44, "new", "trainDestination", LocalDate.now());
        when(trainService.updateTrain(train)).thenReturn(1);

        // when
        mockMvc.perform(post("/train/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));
        verify(trainService).updateTrain(train);
    }

    @Test
    public void shouldNotUpdateTrainBecauseEmptyName() throws Exception {
        LOGGER.info("()");

        // given
        Train train = new Train(44, null, "trainDestination", LocalDate.now());

        // when
        mockMvc.perform(post("/train/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_BLANK_NAME_WARN)));
    }

    @Test
    public void shouldNotUpdateTrainBecauseOverlongName() throws Exception {
        LOGGER.info("()");

        // given
        String trainName = getOverlongName();
        Train train = new Train(44, trainName, "trainDestination", LocalDate.now());

        // when
        mockMvc.perform(post("/train/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_OVERLONG_NAME_WARN)));
    }

    @Test
    public void shouldNotUpdateTrainBecauseOverlongDestinationName() throws Exception {
        LOGGER.info("()");

        // given
        String trainDestination = getOverlongDestinationName();
        Train train = new Train(44, "new", trainDestination, LocalDate.now());

        // when
        mockMvc.perform(post("/train/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(train)))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We don't know how it happened, but there was a mistake in the data you entered.")))
                .andExpect(model().attribute("errorDescription",
                        is(TRAIN_OVERLONG_DESTINATION_NAME_WARN)));
    }

    @Test
    public void shouldDeleteTrainById() throws Exception {
        LOGGER.info("shouldDeleteTrainById()");

        // given
        Integer id = 42;
        when(trainService.deleteById(id)).thenReturn(1);

        // when
        mockMvc.perform(get("/train/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));
        verify(trainService).deleteById(id);
    }

    @Test
    public void shouldReturnErrorPageBecauseDeleteNonexistentTrain() throws Exception {
        LOGGER.info("shouldReturnErrorPageBecauseDeleteNonexistentTrain()");

        // given
        Integer id = 9;
        ErrorMessage message = new ErrorMessage("help");
        when(trainService.deleteById(id)).thenThrow(getNotFoundErrorException(message));

        // when
        mockMvc.perform(get("/train/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find anything about this.")))
                .andExpect(model().attribute("errorDescription",
                        is("help")));
        verify(trainService).deleteById(id);
    }

    @Test
    public void shouldReturnErrorPageBecauseDeleteLoadedTrain() throws Exception {
        LOGGER.info("shouldReturnErrorPageBecauseDeleteLoadedTrain()");

        // given
        Integer id = 3;
        ErrorMessage message = new ErrorMessage("hi-hi");
        when(trainService.deleteById(id)).thenThrow(
                HttpClientErrorException.create(
                        HttpStatus.CONFLICT,
                        "none",
                        null,
                        mapper.writeValueAsString(message).getBytes(),
                        null));

        // when
        mockMvc.perform(get("/train/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage",
                        is("We are sorry, but we cannot remove a loaded train. " +
                                "You must first remove the passenger(s).")))
                .andExpect(model().attribute("errorDescription",
                        is("hi-hi")));
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
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_NAME_LENGTH + 1);
    }

    private String getOverlongDestinationName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_DESTINATION_NAME_LENGTH + 1);
    }
}