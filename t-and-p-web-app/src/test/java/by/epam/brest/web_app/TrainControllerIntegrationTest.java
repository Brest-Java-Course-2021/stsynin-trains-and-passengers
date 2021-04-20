package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class TrainControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private TrainService trainService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnTrainsPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/trains")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
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

    @Test
    public void shouldOpenNewTrainPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("train", isA(Train.class)));
    }

    @Test
    public void shouldAddNewTrain() throws Exception {
        Integer countBefore = trainService.getTrainsCount();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/train")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));

        assertEquals(countBefore + 1, trainService.getTrainsCount());
    }

    @Test
    public void shouldNotAddTrainIfTrainNameExistInBase() throws Exception {
        String testName = "test";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/train")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainName", "test")
        ).andDo(MockMvcResultHandlers.print());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/train")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainName", testName)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Unfortunately a train with name \"" + testName + "\" already exists.")))
        ;
    }

    @Test
    public void shouldOpenToEditTrainPageById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("train"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("train", hasProperty("trainId", is(1))))
                .andExpect(model().attribute("train", hasProperty("trainName", is("first"))))
                .andExpect(model().attribute("train", hasProperty("trainDepartureDate", is(LocalDate.of(1970, 1, 1)))))
                .andExpect(model().attribute("train", hasProperty("trainDestination", is("first direction"))));
    }

    @Test
    public void shouldRedirectToErrorPageIfTrainNotFoundById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for this train.")))
        ;
    }

    @Test
    public void shouldUpdateTrainAfterEdit() throws Exception {
        Integer countBefore = trainService.getTrainsCount();
//        String testName = RandomStringUtils.randomAlphabetic(DEPARTMENT_NAME_SIZE);
        String testName = "TestNameForTrain";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/train/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainId", "1")
                        .param("trainName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));

        Optional<Train> optionalTrain = trainService.findById(1);
        assertTrue(optionalTrain.isPresent());
        assertEquals(testName, optionalTrain.get().getTrainName());
        assertEquals(countBefore, trainService.getTrainsCount());
    }

    @Test
    public void shouldNotUpdateTrainIfNewNameAlreadyExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/train/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainId", "1")
                        .param("trainName", "second")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("Unfortunately a train name \"second\" already exists.")))
        ;
    }

    @Test
    public void shouldDeleteTrainById() throws Exception {
        Integer countBefore = trainService.getTrainsCount();
        Integer idForDelete = trainService.createTrain(new Train("FreeTrain"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/" + idForDelete + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainId", String.valueOf(idForDelete))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/trains"))
                .andExpect(redirectedUrl("/trains"));

        assertEquals(countBefore, trainService.getTrainsCount());
    }

    @Test
    public void shouldReturnErrorPageIfTryToDeleteNonexistentTrain() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/999/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainId", "999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't find record for delete this train.")))
        ;
    }

    @Test
    public void shouldReturnErrorPageIfTryToDeleteLoadedTrain() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("trainId", "999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we can't delete loaded train. You should remove passenger(s) first.")))
        ;
    }

    @Test
    public void shouldReturnTrainsPageWithStartFilter() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/filteredTrains")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateStart", "2020-01-01")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trains"))
                .andExpect(model().attribute("trains", hasSize(2)))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(2)),
                                hasProperty("trainName", is("second"))
                        )
                )))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(3)),
                                hasProperty("trainName", is("third"))
                        )
                )))
        ;
    }

    @Test
    public void shouldReturnTrainsPageWithEndFilter() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/filteredTrains")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateEnd", "2020-02-25")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trains"))
                .andExpect(model().attribute("trains", hasSize(2)))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(1)),
                                hasProperty("trainName", is("first"))
                        )
                )))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(2)),
                                hasProperty("trainName", is("second"))
                        )
                )))
        ;
    }

    @Test
    public void shouldReturnTrainsPageWithBothFilters() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/filteredTrains")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateStart", "2020-01-01")
                        .param("dateEnd", "2020-02-25")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trains"))
                .andExpect(model().attribute("trains", hasSize(1)))
                .andExpect(model().attribute("trains", hasItem(
                        allOf(
                                hasProperty("trainId", is(2)),
                                hasProperty("trainName", is("second"))
                        )
                )))
        ;
    }

    @Test
    public void shouldReturnTrainsPageWithNeitherFilters() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/filteredTrains")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("trains"))
                .andExpect(model().attribute("trains", hasSize(3)))
        ;
    }

    @Test
    public void shouldReturnErrorPageWithWrongFiltersOrder() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/filteredTrains")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateStart", "2020-02-25")
                        .param("dateEnd", "2020-01-01")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/error"))
                .andExpect(model().attribute("errorMessage",
                        is("We're sorry, but we use wrong search parameters.")))
        ;
    }
}