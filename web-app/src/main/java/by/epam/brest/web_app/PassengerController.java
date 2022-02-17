package by.epam.brest.web_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.model.dto.PassengerDto;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.TrainService;
import by.epam.brest.service.rest.PassengerRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PassengerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerController.class);

    private final PassengerDtoService passengerDtoService;

    private final PassengerRestService passengerService;

    private final TrainService trainService;

    @Autowired
    public PassengerController(PassengerDtoService passengerDtoService, PassengerRestService passengerService, TrainService trainService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
        this.trainService = trainService;
        LOGGER.info("PassengerController was created");
    }

    /**
     * Goto passengers list page.
     *
     * @param model model.
     * @return view passengers.
     */
    @GetMapping(value = "/passengers")
    public final String passengers(Model model) {
        LOGGER.info(" IN: passengers() - []");
        List<PassengerDto> passengers = passengerDtoService.findAllPassengersWithTrainName();
        model.addAttribute("passengers", passengers);
        LOGGER.info("OUT: passengers() - [found {} passenger(s)]", passengers);
        return "passengers";
    }

    /**
     * Goto edit passenger page.
     *
     * @param model model.
     * @param id    passenger id.
     * @return view passenger.
     */
    @GetMapping(value = "/passenger/{id}")
    public final String gotoEditPassengerPage(@PathVariable Integer id, Model model) {
        LOGGER.info(" IN: gotoEditPassengerPage() - [{}]", id);
        Passenger passenger = passengerService.findById(id);
        model.addAttribute("isNew", false);
        model.addAttribute("passenger", passenger);
        model.addAttribute("trains", trainService.findAll());
        LOGGER.info("OUT: gotoEditPassengerPage() - [{}]", passenger);
        return "passenger";
    }

    /**
     * Goto add new passenger page.
     *
     * @param model model.
     * @return view passenger.
     */
    @GetMapping(value = "/passenger")
    public final String gotoAddPassengerPage(Model model) {
        LOGGER.info(" IN: gotoAddPassengerPage() - []");
        model.addAttribute("isNew", true);
        model.addAttribute("passenger", new Passenger());
        model.addAttribute("trains", trainService.findAll());
        LOGGER.info("OUT: gotoAddPassengerPage() - [view passenger]");
        return "passenger";
    }

    /**
     * Save new passenger information into storage.
     *
     * @param passenger new passenger data.
     * @return view passengers.
     */
    @PostMapping(value = "/passenger")
    public String addPassenger(@Valid @RequestBody Passenger passenger) {
        LOGGER.info(" IN: addPassenger() - [{}]", passenger);
        Integer id = passengerService.createPassenger(passenger);
        LOGGER.info("OUT: addPassenger() - [new passenger id:{}]", id);
        return "redirect:/passengers";
    }

    /**
     * Update passenger information in storage.
     *
     * @param passenger updated passenger data.
     * @return view passengers.
     */
    @PostMapping(value = "/passenger/{id}")
    public String updatePassenger(@Valid @RequestBody Passenger passenger) {
        LOGGER.info(" IN: updatePassenger() - [{}]", passenger);
        Integer count = passengerService.updatePassenger(passenger);
        LOGGER.info("OUT: updatePassenger() - updated: [{}]", count);
        return "redirect:/passengers";
    }

    /**
     * Delete passenger information in storage.
     *
     * @param id passenger id.
     * @return view passengers.
     */
    @GetMapping(value = "/passenger/{id}/delete")
    public String deletePassenger(@PathVariable Integer id) {
        LOGGER.info(" IN: deletePassenger() - [{}]", id);
        passengerService.deleteById(id);
        LOGGER.info("OUT: deletePassenger() - [deleted]");
        return "redirect:/passengers";
    }
}
