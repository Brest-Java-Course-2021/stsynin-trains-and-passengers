package by.epam.brest.service.rest;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Service
public class TrainRestService implements TrainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestService.class);

    private final String url;

    private final RestTemplate restTemplate;

    public TrainRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Train> findAll() {
        LOGGER.debug("findAll()");

        ResponseEntity<List<Train>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return responseEntity.getBody();
    }

    @Override
    public Train findById(Integer id) {
        LOGGER.debug(" IN: findById() - [{}]", id);

        Train train = restTemplate.getForObject(url + "/{id}", Train.class, id);
        LOGGER.debug("OUT: findById() - [{}]", train);
        return train;
    }

    @Override
    public Integer createTrain(Train train) {
        LOGGER.debug("createTrain()");
        Integer result = restTemplate.postForEntity(
                url,
                train,
                Integer.class).getBody();
        LOGGER.debug("train id: ({}) created", result);
        return result;
    }

    @Override
    public Integer updateTrain(Train train) {
        LOGGER.debug("updateTrain({})", train);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Train> entity = new HttpEntity<>(train, headers);
        Integer result = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Integer.class).getBody();
        LOGGER.debug("train id: ({}) updated", result);
        return result;
    }

    @Override
   public Integer deleteById(Integer id) {
        LOGGER.debug(" IN: deleteById() - [{}]", id);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Train> entity = new HttpEntity<>(headers);
        restTemplate.exchange(
                url + "/" + id,
                HttpMethod.DELETE,
                entity,
                Integer.class);
        LOGGER.debug("OUT: deleteById() - [deleted]");
        return 1;
    }

    @Override
    public Integer getTrainsCount() {
        return null;
    }
}
