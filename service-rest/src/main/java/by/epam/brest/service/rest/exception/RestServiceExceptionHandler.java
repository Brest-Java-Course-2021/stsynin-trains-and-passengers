package by.epam.brest.service.rest.exception;

import by.epam.brest.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String handleNotFoundError(ResourceNotFoundException e, RedirectAttributes redirectAttributes) {
        LOGGER.error(e.getMessage());
        redirectAttributes.addAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }
}
