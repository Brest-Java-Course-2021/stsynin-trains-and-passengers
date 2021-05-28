package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_DESTINATION_NAME_LENGTH;
import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_NAME_LENGTH;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TrainController.class)
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @MockBean
    private TrainDtoService trainDtoService;

    @Test
    public void shouldReturnTrainsPageAfterSearch() throws Exception {
        LocalDate dateStart = LocalDate.of(2010, 10, 10);
        LocalDate dateEnd = LocalDate.of(2011, 11, 11);
        mockMvc.perform(get("/trains")
                .param("dateStart", String.valueOf(dateStart))
                .param("dateEnd", String.valueOf(dateEnd))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("dateStart", is(dateStart)))
                .andExpect(model().attribute("dateEnd", is(dateEnd)))
                .andExpect(view().name("trains"))
        ;
    }

    @Test
    public void shouldReturnErrorPageWithWrongFiltersOrder() throws Exception {
        mockMvc.perform(get("/trains?dateStart=2011-11-11&dateEnd=2010-10-10")
        ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
        ;
    }

    @Test
    public void shouldReturnTrainPageToEdit() throws Exception {

        // given
        Train train = new Train("TrainName");
        Integer id = 1;
        train.setTrainId(id);

        // when
        when(trainService.findById(id)).thenReturn(Optional.of(train));

        mockMvc.perform(get("/train/" + id)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("train", is(train)))
        ;
    }

    @Test
    public void shouldRedirectToErrorPageIfTrainNotFoundById() throws Exception {

        // given
        Integer id = 9;

        // when
        when(trainService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/train/1")
        ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
        ;
    }

    @Test
    public void shouldOpenNewTrainPage() throws Exception {
        mockMvc.perform(get("/train")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(true)))
        ;
    }

    @Test
    public void shouldAddNewTrain() throws Exception {
        mockMvc.perform(post("/train")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", "trainDestination")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"))
        ;
    }

    @Test
    public void shouldNotAddNewTrainBecauseEmptyName() throws Exception {
        mockMvc.perform(post("/train")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", (String) null)
                .param("trainDestination", "trainDestination")
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Train name can't be empty")))
        ;
    }

    @Test
    public void shouldNotAddNewTrainBecauseOverlongName() throws Exception {
        String trainName = getOverlongName();
        mockMvc.perform(post("/train")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", trainName)
                .param("trainDestination", "trainDestination")
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Train name " + trainName + " is too long")))
        ;
    }

    @Test
    public void shouldNotAddNewTrainBecauseEmptyDestinationName() throws Exception {
        mockMvc.perform(post("/train")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", (String) null)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Train destination name can't be empty")))
        ;
    }

    @Test
    public void shouldNotAddNewTrainBecauseOverlongDestinationName() throws Exception {
        String trainDestination = getOverlongDestinationName();
        mockMvc.perform(post("/train")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", trainDestination)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Create fail. Train destination name " + trainDestination + " is too long")))
        ;
    }

    @Test
    public void shouldUpdateTrainAfterEdit() throws Exception {
        mockMvc.perform(post("/train/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", "trainDestination")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"))
        ;
    }

    @Test
    public void shouldNotUpdateTrainBecauseEmptyName() throws Exception {
        mockMvc.perform(post("/train/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", (String) null)
                .param("trainDestination", "trainDestination")
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Train name can't be empty")))
        ;
    }

    @Test
    public void shouldNotUpdateTrainBecauseOverlongName() throws Exception {
        String trainName = getOverlongName();
        mockMvc.perform(post("/train/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", trainName)
                .param("trainDestination", "trainDestination")
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Train name " + trainName + " is too long")))
        ;
    }

    @Test
    public void shouldNotUpdateTrainBecauseEmptyDestinationName() throws Exception {
        mockMvc.perform(post("/train/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", (String) null)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Train destination name can't be empty")))
        ;
    }

    @Test
    public void shouldNotUpdateTrainBecauseOverlongDestinationName() throws Exception {
        String trainDestination = getOverlongDestinationName();
        mockMvc.perform(post("/train/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("trainName", "trainName")
                .param("trainDestination", trainDestination)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Update fail. Train destination name " + trainDestination + " is too long")))
        ;
    }

    @Test
    public void shouldDeleteTrainById() throws Exception {

        // given
        Train train = new Train("TrainName");
        Integer id = 42;
        train.setTrainId(id);

        // when
        when(trainService.findById(id)).thenReturn(Optional.of(train));

        mockMvc.perform(get("/train/" + id + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));
    }

    @Test
    public void shouldReturnErrorPageBecauseDeleteNonexistentTrain() throws Exception {

        // given
        Integer id = 42;

        // when
        when(trainService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/train/" + id + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for delete this train.")))
        ;
    }

    @Test
    public void shouldReturnErrorPageBecauseDeleteLoadedTrain() throws Exception {

        // given
        Train train = new Train("TrainName");
        Integer id = 42;
        train.setTrainId(id);

        // when
        when(trainService.findById(id)).thenReturn(Optional.of(train));
        when(trainService.isTrainLoaded(id)).thenReturn(true);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/" + id + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't delete loaded train. You should remove passenger(s) first.")))
        ;
    }

//TODO    public void shouldNotAddTrainIfTrainNameExistInBase() throws Exception {

//TODO    public void shouldNotUpdateTrainIfNewNameAlreadyExists() throws Exception {

    private String getOverlongName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_NAME_LENGTH + 1);
    }

    private String getOverlongDestinationName() {
        return RandomStringUtils.randomAlphabetic(MAX_TRAIN_DESTINATION_NAME_LENGTH + 1);
    }
}