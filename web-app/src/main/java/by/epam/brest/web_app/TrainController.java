package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_DESTINATION_NAME_LENGTH;
import static by.epam.brest.model.constants.TrainConstants.MAX_TRAIN_NAME_LENGTH;

@Controller
public class TrainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainController.class);

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
        LOGGER.debug("user ask trains list from: {} to: {}", dateStart, dateEnd);
        if (!(dateStart == null) && !(dateEnd == null) && dateEnd.isBefore(dateStart)) {
            LOGGER.error("wrong filters order! alarm!!!");
            redirectAttributes.addAttribute("errorMessage",
                    "We're sorry, but we use wrong search parameters.");
            return "redirect:/error";
        } else {
            LOGGER.debug("return result of search");
            model.addAttribute("trains", trainDtoService.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd));
            model.addAttribute("dateStart", dateStart);
            model.addAttribute("dateEnd", dateEnd);
            return "trains";
        }
    }

    /**
     * Goto edit train page.
     *
     * @param id train id.
     * @return view train.
     */
    @GetMapping(value = "/train/{id}")
    public final String gotoEditTrainPage(@PathVariable Integer id, Model model) {
        LOGGER.info(" IN: gotoEditTrainPage() - [{}]", id);
        Train train = trainService.findById(id);
        model.addAttribute("isNew", false);
        model.addAttribute("train", train);
        LOGGER.info("OUT: gotoEditTrainPage() - [{}]", train);
        return "train";
    }

    /**
     * Goto add new train page.
     *
     * @param model model.
     * @return view train.
     */
    @GetMapping(value = "/train")
    public final String gotoAddTrainPage(Model model) {
        LOGGER.debug("Create new train");
        model.addAttribute("isNew", true);
        model.addAttribute("train", new Train());
        return "train";
    }

    /**
     * Save new train information into storage. Check name & destination for null and length.
     *
     * @param train filled new train data.
     * @return view trains or view error.
     */
    @PostMapping(value = "/train")
    public String addTrain(Train train,
                           RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to save new train");
        String errorWithTrainNames = getErrorWithTrainNames(train, "Create");
        if (errorWithTrainNames != null) {
            LOGGER.error(errorWithTrainNames);
            redirectAttributes.addAttribute("errorMessage", errorWithTrainNames);
            return "redirect:/error";
        } else {
            LOGGER.debug("creating {}", train);
            this.trainService.createTrain(train);
            return "redirect:/trains";
        }
    }

    /**
     * Update train information in storage. Check name & destination for null and length.
     *
     * @param train updated train data.
     * @return view trains or view error.
     */
    @PostMapping(value = "/train/{id}")
    public String updateTrain(Train train,
                              RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to update train");
        String errorWithTrainNames = getErrorWithTrainNames(train, "Update");
        if (errorWithTrainNames != null) {
            LOGGER.error(errorWithTrainNames);
            redirectAttributes.addAttribute("errorMessage", errorWithTrainNames);
            return "redirect:/error";
        } else {
            LOGGER.debug("updating {}", train);
            this.trainService.updateTrain(train);
            return "redirect:/trains";
        }
    }

    /**
     * Delete train information in storage.
     * If train isn't exist - goto error page.
     * if train loaded - goto error page.
     *
     * @param model model.
     * @param id    train id.
     * @return view trains or view error.
     */
    @GetMapping(value = "/train/{id}/delete")
    public String deleteTrain(@PathVariable Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        LOGGER.debug("user ask to delete train id: {}", id);
        Optional<Train> optionalTrain = trainService.findById(id);
        if (optionalTrain.isPresent()) {
            if (trainService.isTrainLoaded(id)) {
                LOGGER.error("...but train id: {} is loaded", id);
                redirectAttributes.addAttribute(
                        "errorMessage",
                        "We're sorry, but we can't delete loaded train. You should remove passenger(s) first.");
                return "redirect:/error";
            }
            LOGGER.debug("execute delete");
            this.trainService.deleteTrain(id);
            return "redirect:/trains";
        } else {
            LOGGER.error("...but train id: {} was not found", id);
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "We're sorry, but we can't find record for delete this train.");
            return "redirect:/error";
        }
    }

    private String getErrorWithTrainNames(Train train, String stage) {
        String trainName = train.getTrainName();
        String trainDestination = train.getTrainDestination();
        if (trainName == null) {
            return stage + " fail. Train name can't be empty";
        }
        if (trainDestination == null) {
            return stage + " fail. Train destination name can't be empty";
        }
        if (trainNameIsOverlong(trainName)) {
            return stage + " fail. Train name " + trainName + " is too long";
        }
        if (trainDestinationNameIsOverlong(trainDestination)) {
            return stage + " fail. Train destination name " + trainDestination + " is too long";
        }
        return null;
    }

    private boolean trainNameIsOverlong(String name) {
        return name.length() > MAX_TRAIN_NAME_LENGTH;
    }

    private boolean trainDestinationNameIsOverlong(String destination) {
        return destination.length() > MAX_TRAIN_DESTINATION_NAME_LENGTH;
    }
}
