package by.epam.brest.service.rest_app;

import by.epam.brest.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Tsynin
 */
@RestController
public class ErrorRestController implements ErrorController {

    public ErrorRestController() {
        LOGGER.info("ErrorRestController was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorRestController.class);

    @RequestMapping("/error")
    public ResponseEntity<ErrorMessage> handleError(HttpServletRequest request) {

        LOGGER.error("error detected!!!");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            LOGGER.error(" statusCode: {}", statusCode);

            switch (statusCode) {
                case 404: {
                    LOGGER.error(" Resource not found");
                    return new ResponseEntity<>(
                            new ErrorMessage("Resource not found"),
                            HttpStatus.NOT_FOUND);
                }
                case 500: {
                    LOGGER.error(" Internal Server Error");
                    return new ResponseEntity<>(
                            new ErrorMessage("Internal Server Error"),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        LOGGER.error(" Unknown error");
        return new ResponseEntity<>(
                new ErrorMessage("Unknown error"),
                HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}
