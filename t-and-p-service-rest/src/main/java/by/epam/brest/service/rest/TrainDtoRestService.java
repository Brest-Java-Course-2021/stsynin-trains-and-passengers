package by.epam.brest.service.rest;

import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.service.TrainDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
public class TrainDtoRestService implements TrainDtoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainRestService.class);

    private String url;

    private RestTemplate restTemplate;

    public TrainDtoRestService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Get trains from the database with counts of passengers from a period of time.
     *
     * @param dateStart start of period.
     * @param dateEnd   end of period.
     * @return trains list.
     */
    @Override
    public List<TrainDto> getFilteredByDateTrainListWithPassengersCount(LocalDate dateStart, LocalDate dateEnd) {
        LOGGER.debug("get trains (from {} to {} ) with passengers count", dateStart, dateEnd);
        ResponseEntity<List<TrainDto>> result = restTemplate.exchange(
                url + getDatesAsParameter(dateStart, dateEnd),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return result.getBody();
    }

    private String getDatesAsParameter(LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder params = new StringBuilder("?dateStart=");
        if (dateStart != null) {
            params.append(dateStart);
        }
        params.append("&dateEnd=");
        if (dateEnd != null) {
            params.append(dateEnd);
        }
        return params.toString();
    }
}
