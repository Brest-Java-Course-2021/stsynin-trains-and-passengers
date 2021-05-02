package by.epam.brest.service.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @author Sergey Tsynin
 */
@Component
public class ErrorRestService implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorRestService.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
            LOGGER.error("SERVER_ERROR");
        } else if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            LOGGER.error("CLIENT_ERROR");
            LOGGER.error(String.valueOf(httpResponse.getStatusCode()));
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                LOGGER.error("not found");
            }
            if (httpResponse.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                LOGGER.error("wow! UNPROCESSABLE_ENTITY");
            }
        }
    }
}
