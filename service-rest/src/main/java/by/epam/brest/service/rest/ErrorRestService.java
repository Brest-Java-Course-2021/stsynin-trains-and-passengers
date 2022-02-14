package by.epam.brest.service.rest;

import by.epam.brest.model.ErrorMessage;
import by.epam.brest.service.exception.ResourceLockedException;
import by.epam.brest.service.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @author Sergey Tsynin
 */
@Component
public class ErrorRestService implements ResponseErrorHandler {

    public ErrorRestService() {
        LOGGER.info("ErrorRestService was created");
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

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
            LOGGER.debug("SERVER_ERROR");
        } else if (httpResponse.getStatusCode()
                .series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            LOGGER.debug("CLIENT_ERROR");
            LOGGER.debug(String.valueOf(httpResponse.getStatusCode()));
            String message = extractErrorMessage(httpResponse).getMessage();
            LOGGER.error("message from server - " + message);
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException(message);
            }
            if (httpResponse.getStatusCode() == HttpStatus.LOCKED) {
                throw new ResourceLockedException(message);
            }
            if (httpResponse.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                LOGGER.error("wow! UNPROCESSABLE_ENTITY");
            }
        }
    }

    private ErrorMessage extractErrorMessage(ClientHttpResponse httpResponse) throws IOException {
        return objectMapper.readValue(httpResponseToString(httpResponse), ErrorMessage.class);
    }

    private String httpResponseToString(ClientHttpResponse httpResponse) throws IOException {
        return StreamUtils.copyToString(httpResponse.getBody(), StandardCharsets.UTF_8);
    }
}
