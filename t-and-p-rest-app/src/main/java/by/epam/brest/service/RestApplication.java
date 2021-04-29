package by.epam.brest.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Artsiom Prokharau
 */
@SpringBootApplication
@ComponentScan(basePackages = "by.epam.brest")
@PropertySource({"classpath:sql-requests.properties"})
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }
}
