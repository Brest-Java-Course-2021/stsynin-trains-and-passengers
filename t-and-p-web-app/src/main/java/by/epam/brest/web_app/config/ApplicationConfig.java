package by.epam.brest.web_app.config;

import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sergey Tsynin
 */
@Configuration
@ComponentScan
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        restTemplate.setErrorHandler(new ErrorRestService());
        return restTemplate;
    }

    @Bean
    TrainDtoService trainDtoService() {
        String url = String.format("%s://%s:%d/trains-dtos", protocol, host, port);
        return new TrainDtoRestService(url, restTemplate());
    }

    @Bean
    TrainService trainService() {
        String url = String.format("%s://%s:%d/trains", protocol, host, port);
        return new TrainRestService(url, restTemplate());
    }

    @Bean
    PassengerDtoService passengerDtoService() {
        String url = String.format("%s://%s:%d/passengers-dtos", protocol, host, port);
        return new PassengerDtoRestService(url, restTemplate());
    }

    @Bean
    PassengerService passengerService() {
        String url = String.format("%s://%s:%d/passengers", protocol, host, port);
        return new PassengerRestService(url, restTemplate());
    }
}
