package by.epam.brest.web_app;

import by.epam.brest.model.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Sergey Tsynin
 */
@ControllerAdvice
public class WebExceptionHandler {

    public WebExceptionHandler() {
        LOGGER.info("RestServiceExceptionHandler was created");
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ModelAndView handleNotFoundError(HttpClientErrorException.NotFound e) throws IOException {
        String errorMessage = extractErrorMessage(e.getResponseBodyAsString()).getMessage();
        LOGGER.error("OUT: handleNotFoundError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We're sorry, but we can't find anything about this.");
        errorPage.addObject("errorDescription", errorMessage);
        return errorPage;
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ModelAndView handleResourceLockedError(HttpClientErrorException.Conflict e) throws IOException {
        String errorMessage = extractErrorMessage(e.getResponseBodyAsString()).getMessage();
        LOGGER.error("OUT: handleResourceLockedError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We're sorry, but we can't delete loaded train. You should remove passenger(s) first.");
        errorPage.addObject("errorDescription", errorMessage);
        return errorPage;
    }

//    @ExceptionHandler(ConnectException.class)
//    public ModelAndView handleConnectException(ConnectException e) {
//        String errorMessage = e.getMessage();
//        LOGGER.error("OUT: handleConnectException - [{}]", errorMessage);
//        ModelAndView errorPage = new ModelAndView("error");
//        errorPage.addObject("errorMessage",
//                "Internal Server Error");
//        errorPage.addObject("errorDescription", errorMessage);
//        return errorPage;
//    }

    private ErrorMessage extractErrorMessage(String message) throws IOException {
        return objectMapper.readValue(message, ErrorMessage.class);
    }
}
