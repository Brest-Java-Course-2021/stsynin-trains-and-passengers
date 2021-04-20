package by.epam.brest.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Artsiom Prokharau 20.04.2021
 */

@SpringBootApplication
@PropertySource({"classpath:sql-requests.properties"})
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }
}