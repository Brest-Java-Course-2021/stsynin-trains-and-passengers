package by.epam.brest.service.rest.config;

import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.PassengerDtoRestService;
import by.epam.brest.service.rest.PassengerRestService;
import by.epam.brest.service.rest.TrainDtoRestService;
import by.epam.brest.service.rest.TrainRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sergey Tsynin
 */
@Configuration
public class TestConfig {

    public static final String PASSENGERS_URL = "http://localhost:8088/passengers";
    public static final String TRAINS_URL = "http://localhost:8088/trains";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    TrainService trainService() {
        return new TrainRestService(TRAINS_URL, restTemplate());
    }

    @Bean
    TrainDtoService trainDtoService() {
        return new TrainDtoRestService(TRAINS_URL, restTemplate());
    }

    @Bean
    PassengerService passengerService() {
        return new PassengerRestService(PASSENGERS_URL, restTemplate());
    }

    @Bean
    PassengerDtoService passengerDtoService() {
        return new PassengerDtoRestService(PASSENGERS_URL, restTemplate());
    }
}
