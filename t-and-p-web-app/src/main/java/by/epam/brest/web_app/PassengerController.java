package by.epam.brest.web_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static by.epam.brest.model.constants.PassengerConstants.MAX_PASSENGER_NAME_LENGTH;

@Controller
public class PassengerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainController.class);

    private final PassengerDtoService passengerDtoService;

    private final PassengerService passengerService;

    private final TrainService trainService;

    @Autowired
    public PassengerController(PassengerDtoService passengerDtoService, PassengerService passengerService, TrainService trainService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
        this.trainService = trainService;
    }

    /**
     * Goto passengers list page.
     *
     * @param model model.
     * @return view passengers.
     */
    @GetMapping(value = "/passengers")
    public final String passenger(Model model) {
        LOGGER.debug("user ask passengers list");
        model.addAttribute("passengers", passengerDtoService.findAllPassengersWithTrainName());
        return "passengers";
    }

    /**
     * Goto edit passenger page. If passenger record not found - goto error page.
     *
     * @param model model.
     * @param id    passenger id.
     * @return view passenger or view error.
     */
    @GetMapping(value = "/passenger/{id}")
    public final String gotoEditPassengerPage(@PathVariable Integer id,
                                              Model model,
                                              RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask passenger id: {}", id);
        Optional<Passenger> optionalPassenger = passengerService.findById(id);
        if (optionalPassenger.isPresent()) {
            LOGGER.debug("return passenger id: {}", id);
            model.addAttribute("isNew", false);
            model.addAttribute("passenger", optionalPassenger.get());
            model.addAttribute("trains", trainService.findAll());
            return "passenger";
        } else {
            LOGGER.error("passenger id: {} not found", id);
            redirectAttributes.addAttribute("errorMessage",
                    "We're sorry, but we can't find record for this passenger.");
            return "redirect:/error";
        }
    }

    /**
     * Goto add new passenger page.
     *
     * @param model model.
     * @return view passenger.
     */
    @GetMapping(value = "/passenger")
    public final String gotoAddPassengerPage(Model model) {
        LOGGER.debug("Create new passenger");
        model.addAttribute("isNew", true);
        model.addAttribute("passenger", new Passenger());
        model.addAttribute("trains", trainService.findAll());
        return "passenger";
    }

    /**
     * Save new passenger information into storage. Check name for null and length.
     *
     * @param passenger filled new passenger data.
     * @return view passengers.
     */
    @PostMapping(value = "/passenger")
    public String addPassenger(Passenger passenger,
                               RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to save new passenger");
        String errorWithPassengerName = getErrorWithPassengerNames(passenger, "Create");
        if (errorWithPassengerName != null) {
            LOGGER.error(errorWithPassengerName);
            redirectAttributes.addAttribute("errorMessage", errorWithPassengerName);
            return "redirect:/error";
        } else {
            LOGGER.debug("creating {}", passenger);
            if (this.passengerService.createPassenger(passenger) == 0) {
                LOGGER.error("service return error, passenger not created");
                redirectAttributes.addAttribute("errorMessage",
                        "We are very sorry, but the entry has not been made ");
                return "redirect:/error";
            } else {
                LOGGER.debug("Passenger created successfully. id: {}", passenger.getPassengerId());
                return "redirect:/passengers";
            }
        }
    }

    /**
     * Update passenger information in storage Check name for null and length.
     *
     * @param passenger updated passenger data.
     * @return view passengers.
     */
    @PostMapping(value = "/passenger/{id}")
    public String updatePassenger(Passenger passenger,
                                  RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to update passenger");
        String errorWithPassengerName = getErrorWithPassengerNames(passenger, "Update");
        if (errorWithPassengerName != null) {
            LOGGER.error(errorWithPassengerName);
            redirectAttributes.addAttribute("errorMessage", errorWithPassengerName);
            return "redirect:/error";
        } else {
            LOGGER.debug("updating {}", passenger);
            if(this.passengerService.updatePassenger(passenger)==0){
                LOGGER.error("service return error, passenger not created");
                redirectAttributes.addAttribute("errorMessage",
                        "We are very sorry, but the entry has not been updated");
                return "redirect:/error";
            }
            LOGGER.debug("Passenger updated successfully. id: {}", passenger.getPassengerId());
            return "redirect:/passengers";
        }
    }

    /**
     * Delete passenger information in storage.
     * If passenger isn't exist - goto error page.
     *
     * @param model model.
     * @param id    passenger id.
     * @return view passengers.
     */
    @GetMapping(value = "/passenger/{id}/delete")
    public String deletePassenger(@PathVariable Integer id,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to delete passenger id: {}", id);
        Optional<Passenger> optionalPassenger = passengerService.findById(id);
        if (optionalPassenger.isPresent()) {
            LOGGER.debug("execute delete");
            this.passengerService.deletePassenger(id);
            return "redirect:/passengers";
        } else {
            LOGGER.error("...but passenger id: {} was not found", id);
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "We're sorry, but we can't find record for delete this passenger.");
            return "redirect:/error";
        }
    }

    private String getErrorWithPassengerNames(Passenger passenger, String stage) {
        String passengerName = passenger.getPassengerName();
        if (passengerName == null) {
            return stage + " fail. Passenger name can't be empty";
        }
        if (passengerNameIsOverlong(passengerName)) {
            return stage + " fail. Passenger name " + passengerName + " is too long";
        }
        return null;
    }

    private boolean passengerNameIsOverlong(String name) {
        return name.length() > MAX_PASSENGER_NAME_LENGTH;
    }
}
