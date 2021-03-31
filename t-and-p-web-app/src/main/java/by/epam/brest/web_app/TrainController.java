package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class TrainController {

    private final TrainDtoService trainDtoService;

    private final TrainService trainService;

    @Autowired
    public TrainController(TrainDtoService trainDtoService, TrainService trainService) {
        this.trainDtoService = trainDtoService;
        this.trainService = trainService;
    }

    /**
     * Goto trains list page.
     *
     * @param model model.
     * @return view trains.
     */

    @GetMapping(value = "/trains")
    public final String trains(Model model) {
        model.addAttribute("trains", trainDtoService.findAllWithPassengersCount());
        return "trains";
    }

    /**
     * Goto edit train page.
     *
     * @param model model.
     * @param id    train id.
     * @return view train.
     */
    @GetMapping(value = "/train/{id}")
    public final String gotoEditTrainPage(@PathVariable Integer id, Model model) {
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("train", optionalTrain.get());
            return "train";
        } else {
            // TODO train not found - pass error message as parameter or handle not found error
            // polite form
            return "redirect:trains";
        }
    }

    /**
     * Goto add new train page.
     *
     * @param model model.
     * @return view train.
     */
    @GetMapping(value = "/train")
    public final String gotoAddTrainPage(Model model) {
        model.addAttribute("isNew", true);
        model.addAttribute("train", new Train());
        return "train";
    }

    /**
     * Save new train information into storage.
     *
     * @param train filled new train data.
     * @return view trains.
     */
    @PostMapping(value = "/train")
    public String addTrain(Train train) {
        this.trainService.createTrain(train);
        return "redirect:/trains";
    }

    /**
     * Update train information in storage.
     *
     * @param train updated train data.
     * @return view trains.
     */
    @PostMapping(value = "/train/{id}")
    public String updateTrain(Train train) {
        this.trainService.updateTrain(train);
        return "redirect:/trains";
    }
}
