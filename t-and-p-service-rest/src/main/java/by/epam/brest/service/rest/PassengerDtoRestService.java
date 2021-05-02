package by.epam.brest.service.rest;

import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Service
public class PassengerDtoRestService implements PassengerDtoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerDtoRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public PassengerDtoRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get all passenger with connected train name.
     *
     * @return passenger list
     */
    @Override
    public List<PassengerDto> findAllPassengersWithTrainName() {
        LOGGER.debug("get passengers list with trains names");
        ResponseEntity<List<PassengerDto>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return result.getBody();
    }
}
