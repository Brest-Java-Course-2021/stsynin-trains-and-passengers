package by.epam.brest.service.rest_app;

import by.epam.brest.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/error")
    public ResponseEntity<ErrorMessage> handleError(HttpServletRequest request) {

        LOGGER.error(" IN: handleError() - []");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            LOGGER.error(" Error status code: {}", statusCode);

            if (statusCode == 404) {
                LOGGER.error("OUT: handleError() - [Resource [{}] was not found]", uri);
                return new ResponseEntity<>(
                        new ErrorMessage("Resource [" + uri + "] was not found"),
                        HttpStatus.NOT_FOUND);
            }
        }
        LOGGER.error("OUT: handleError() - [Unknown error while [{}] request]", uri);
        return new ResponseEntity<>(
                new ErrorMessage("Unknown error while [" + uri + "] request"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}
