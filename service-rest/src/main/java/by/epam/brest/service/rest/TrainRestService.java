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
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@Service
public class TrainRestService implements TrainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public TrainRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get all trains from the database.
     *
     * @return trains list.
     */
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

    /**
     * Get train by Id.
     *
     * @param trainId train Id.
     * @return train.
     */
    @Override
    public Optional<Train> findById(Integer trainId) {
        LOGGER.debug("findById({})", trainId);
        ResponseEntity<Train> responseEntity =
                restTemplate.getForEntity(
                        url + "/" + trainId,
                        Train.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Save new train record.
     *
     * @param train object.
     * @return saved train Id.
     */
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

    /**
     * Update train record in the database.
     *
     * @param train object.
     * @return number of updated trains in the database.
     */
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

    /**
     * Delete train by Id.
     *
     * @param trainId train Id.
     * @return number of deleted trains in the database.
     */
    @Override
    public Integer deleteTrain(Integer trainId) {
        LOGGER.debug("deleteTrain({})", trainId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Train> entity = new HttpEntity<>(headers);
        Integer result = restTemplate.exchange(
                url + "/" + trainId,
                HttpMethod.DELETE,
                entity,
                Integer.class).getBody();
        LOGGER.debug("trains deleted: ({})", result);
        return result;
    }

    /**
     * Get numbers of trains in the database.
     *
     * @return numbers of trains in the database.
     */
    @Override
    public Integer getTrainsCount() {
        return null;
    }

    /**
     * Check for another train with the same name.
     *
     * @param train object.
     * @return train with the same name presence.
     */
    @Override
    public boolean isSecondTrainWithSameNameExists(Train train) {
        return false;
    }

    /**
     * Check if train have passengers.
     *
     * @param trainId train Id.
     * @return the presence of passengers on this train.
     */
    @Override
    public boolean isTrainLoaded(Integer trainId) {
        return false;
    }
}
