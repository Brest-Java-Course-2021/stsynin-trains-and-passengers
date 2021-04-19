package by.epam.brest.web_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Sergey Tsynin
 */
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = {"classpath:app-context-test.xml"})
//@Transactional
class ErrorControllerIntegrationTest {

//    @Autowired
//    private WebApplicationContext wac;
//
////    @Autowired
////    private TrainService trainService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//    }
////TODO FIX ALL
////    @Test
////    void shouldTranslateErrorMessageIntoErrorPage() throws Exception {
////        TrainService trainService =mock(TrainService.class);
////        doThrow(IllegalStateException.class)
////                .when(trainService.findAll());
////
////        trainService.findAll();
//
////        String exceptionParam = "not_found";
////        when(trainService.findAll()).thenThrow(new IllegalArgumentException("FOO!"));
////
////        mockMvc.perform(
////                MockMvcRequestBuilders.get("/trains")
////
////        ).andDo(MockMvcResultHandlers.print())
////
////                .andExpect(MockMvcResultMatchers.status().isOk())
//
////                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
////                        .param("errorMessage", "testErrorMessage")
////                .andExpect(MockMvcResultMatchers.status())
////                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
////                .andExpect(view().name("errors"))
////        ;
////    }
}