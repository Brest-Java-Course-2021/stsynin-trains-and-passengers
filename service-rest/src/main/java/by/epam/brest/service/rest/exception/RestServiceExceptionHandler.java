package by.epam.brest.service.rest.exception;

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
        errorPage.addObject("errorMessage", "We're sorry, but we can't find anything about this.");
        errorPage.addObject("errorDescription", errorMessage);
        return errorPage;
    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public String handleNotFoundError(ResourceNotFoundException e, RedirectAttributes redirectAttributes) {
//        LOGGER.error(e.getMessage());
//        redirectAttributes.addAttribute("errorMessage", e.getMessage());
//        return "redirect:/error";
//    }
}
