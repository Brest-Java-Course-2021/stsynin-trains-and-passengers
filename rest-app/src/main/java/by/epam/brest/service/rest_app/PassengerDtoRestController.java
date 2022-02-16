package by.epam.brest.service.rest_app;

import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
public class PassengerDtoRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerDtoRestController.class);

    private final PassengerDtoService passengerDtoService;

    public PassengerDtoRestController(PassengerDtoService passengerDtoService) {
        this.passengerDtoService = passengerDtoService;
    }

    /**
     * Endpoint "/passengers-dtos". Passengers list with connected trains names.
     *
     * @return PassengerDtos list.
     */
    @GetMapping(value = "/passengers-dtos", produces = {"application/json"})
    public final List<PassengerDto> getAllDtos() {
        LOGGER.info(" IN: getAllDtos() - []");
        List<PassengerDto> passengers = passengerDtoService.findAllPassengersWithTrainName();
        LOGGER.info("OUT: getAllDtos() - [found {} passenger(s)]", passengers.size());
        return passengers;
    }
}
