package by.epam.brest.service.rest_app;

import by.epam.brest.service.rest_app.exception.ErrorResponse;
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

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404: {
                    return new ResponseEntity<>(
                            new ErrorResponse("404", "Resource not found"),
                            HttpStatus.NOT_FOUND);
                }
                case 500: {
                    return new ResponseEntity<>(
                            new ErrorResponse("500", "Internal Server Error"),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>(
                new ErrorResponse("501", "Unknown error"),
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
