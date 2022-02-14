package by.epam.brest.service.rest.exception;

import by.epam.brest.service.exception.ResourceLockedException;
import by.epam.brest.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Sergey Tsynin
 */
@ControllerAdvice
public class RestServiceExceptionHandler {

    public RestServiceExceptionHandler() {
        LOGGER.info("RestServiceExceptionHandler was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFoundError(ResourceNotFoundException e) {
        String errorMessage = e.getMessage();
        LOGGER.error("OUT: handleNotFoundError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We're sorry, but we can't find anything about this.");
        errorPage.addObject("errorDescription", errorMessage);
        return errorPage;
    }

    @ExceptionHandler(ResourceLockedException.class)
    public ModelAndView handleResourceLockedError(ResourceLockedException e) {
        String errorMessage = e.getMessage();
        LOGGER.error("OUT: handleResourceLockedError - [{}]", errorMessage);
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("errorMessage",
                "We're sorry, but we can't delete loaded train. You should remove passenger(s) first.");
        errorPage.addObject("errorDescription", errorMessage);
        return errorPage;
    }
}
