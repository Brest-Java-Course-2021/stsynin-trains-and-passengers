package by.epam.brest.web_app;

import by.epam.brest.service.TrainDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrainController {

    private final TrainDtoService trainDtoService;

    @Autowired
    public TrainController(TrainDtoService trainDtoService) {
        this.trainDtoService = trainDtoService;
    }

    /**
     * Goto trains list page.
     *
     * @param model model.
     * @return view.
     */

    @GetMapping(value = "/trains")
    public final String trains(Model model) {
        model.addAttribute("trains", trainDtoService.findAllWithPassengersCount());
        return "trains";
    }

    /**
     * Goto edit train page.
     *
     * @return
     */
    @GetMapping(value = "/train/{id}")
    public final String gotoEditTrainPage(@PathVariable Integer id, Model model) {
        model.addAttribute("mode", "edit");
        return "train";
    }

    /**
     * Goto add train page.
     *
     * @return
     */
    @GetMapping(value = "/train/add")
    public final String gotoAddTrainPage(Model model) {
        model.addAttribute("mode", "add new");
        return "train";
    }
}
