package by.epam.brest.web_app;

import by.epam.brest.model.ErrorMessage;
import by.epam.brest.model.exception.ValidationErrorException;
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
        LOGGER.info("WebExceptionHandler was created");
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ModelAndView handleNotFoundError(HttpClientErrorException.NotFound e) throws IOException {
        String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
        LOGGER.error(" IN: handleNotFoundError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We're sorry, but we can't find anything about this.");
        errorPage.addObject("errorDescription", errorMessage);
        LOGGER.error("OUT: handleNotFoundError - [errorPage]");
        return errorPage;
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ModelAndView handleResourceLockedError(HttpClientErrorException.Conflict e) throws IOException {
        String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
        LOGGER.error(" IN: handleResourceLockedError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We are sorry, but we cannot remove a loaded train. " +
                        "You must first remove the passenger(s).");
        errorPage.addObject("errorDescription", errorMessage);
        LOGGER.error("OUT: handleResourceLockedError - [errorPage]");
        return errorPage;
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ModelAndView handleValidationError(ValidationErrorException e) throws IOException {
        String errorMessage = e.getMessage();
        LOGGER.error(" IN: handleValidationError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We don't know how it happened, but there was a mistake in the data you entered.");
        errorPage.addObject("errorDescription", errorMessage);
        LOGGER.error("OUT: handleValidationError - [errorPage]");
        return errorPage;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleOtherException(Exception e) {
        String errorMessage = e.getMessage();
        LOGGER.error("OUT: handleOtherException - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage", "Unknown Error");
        errorPage.addObject("errorDescription", errorMessage);
        LOGGER.error("OUT: handleOtherException - [errorPage]");
        return errorPage;
    }

    private String extractErrorMessage(String message) throws IOException {
        if (message.length() == 0) {
            return "no comments";
        }
        return objectMapper.readValue(message, ErrorMessage.class).getMessage();
    }
}
