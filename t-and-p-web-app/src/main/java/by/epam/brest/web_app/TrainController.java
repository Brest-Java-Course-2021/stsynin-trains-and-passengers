package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
     * @param dateStart start of period of time.
     * @param dateEnd   end of period of time.
     * @param model     model.
     * @return view trains or view error.
     */
    @GetMapping(value = "/trains")
    public final String trainsWithFilter(@RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        if (!(dateStart == null) && !(dateEnd == null) && dateEnd.isBefore(dateStart)) {
            redirectAttributes.addAttribute("errorMessage",
                    "We're sorry, but we use wrong search parameters.");
            return "redirect:/error";
        } else {
            model.addAttribute("trains", trainDtoService.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd));
            model.addAttribute("dateStart", dateStart);
            model.addAttribute("dateEnd", dateEnd);
            return "trains";
        }
    }

    /**
     * Goto edit train page. If train record not found - goto error page.
     *
     * @param model model.
     * @param id    train id.
     * @return view train.
     */
    @GetMapping(value = "/train/{id}")
    public final String gotoEditTrainPage(@PathVariable Integer id,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("train", optionalTrain.get());
            return "train";
        } else {
            redirectAttributes.addAttribute("errorMessage",
                    "We're sorry, but we can't find record for this train.");
            return "redirect:/error";
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
     * Save new train information into storage. If train name already exist - goto error page.
     *
     * @param train filled new train data.
     * @return view trains or view error.
     */
    @PostMapping(value = "/train")
    public String addTrain(Train train,
                           RedirectAttributes redirectAttributes) {
        if (!this.trainService.isSecondTrainWithSameNameExists(train)) {
            this.trainService.createTrain(train);
            return "redirect:/trains";
        } else {
            redirectAttributes.addAttribute("errorMessage",
                    "Unfortunately a train with name \"" + train.getTrainName() + "\" already exists.");
            return "redirect:/error";
        }
    }

    /**
     * Update train information in storage. If train name already exist - goto error page.
     *
     * @param train updated train data.
     * @return view trains or view error.
     */
    @PostMapping(value = "/train/{id}")
    public String updateTrain(Train train,
                              RedirectAttributes redirectAttributes) {
        if (!this.trainService.isSecondTrainWithSameNameExists(train)) {
            this.trainService.updateTrain(train);
            return "redirect:/trains";
        } else {
            redirectAttributes.addAttribute("errorMessage",
                    "Unfortunately a train name \"" + train.getTrainName() + "\" already exists.");
            return "redirect:/error";
        }
    }

    /**
     * Delete train information in storage. If train isn't exist - goto error page.
     *
     * @param model model.
     * @param id    train id.
     * @return view trains or view error.
     */
    @GetMapping(value = "/train/{id}/delete")
    public String deleteTrain(@PathVariable Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isPresent()) {
            if (trainService.isTrainLoaded(id)) {
                redirectAttributes.addAttribute(
                        "errorMessage",
                        "We're sorry, but we can't delete loaded train. You should remove passenger(s) first.");
                return "redirect:/error";
            }
            this.trainService.deleteTrain(id);
            return "redirect:/trains";
        } else {
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "We're sorry, but we can't find record for delete this train.");
            return "redirect:/error";
        }
    }
}
