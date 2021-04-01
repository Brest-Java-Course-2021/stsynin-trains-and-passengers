package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
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
    public void shouldReturnToTrainsPageIfTrainNotFoundById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/train/999")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("trains"));
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

        Optional<Train> optionalDepartment = trainService.findById(1);
        assertTrue(optionalDepartment.isPresent());
        assertEquals(testName, optionalDepartment.get().getTrainName());
        assertEquals(countBefore, trainService.getTrainsCount());
    }
}