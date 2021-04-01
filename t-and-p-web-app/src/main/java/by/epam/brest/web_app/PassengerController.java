package by.epam.brest.web_app;

import by.epam.brest.model.Passenger;
import by.epam.brest.service.PassengerDtoService;
import by.epam.brest.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PassengerController {

    private final PassengerDtoService passengerDtoService;

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerDtoService passengerDtoService, PassengerService passengerService) {
        this.passengerDtoService = passengerDtoService;
        this.passengerService = passengerService;
    }

    /**
     * Goto passengers list page.
     *
     * @param model model.
     * @return view passengers.
     */
    @GetMapping(value = "/passengers")
    public final String passengers(Model model) {
        model.addAttribute("passengers", passengerDtoService.findAllPassengersWithTrainName());
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
        Optional<Passenger> optionalPassenger = passengerService.findById(id);
        if (optionalPassenger.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("passenger", optionalPassenger.get());
            return "passenger";
        } else {
            // TODO Passenger not found - pass error message as parameter or handle not found error
            // polite form
            return "redirect:passengers";
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
        model.addAttribute("isNew", true);
        model.addAttribute("passenger", new Passenger());
        return "passenger";
    }
}
