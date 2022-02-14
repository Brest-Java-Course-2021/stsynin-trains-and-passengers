package by.epam.brest.web_app;

import by.epam.brest.model.Train;
import by.epam.brest.model.dto.TrainDto;
import by.epam.brest.model.exception.ValidationErrorException;
import by.epam.brest.service.TrainDtoService;
import by.epam.brest.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

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
        LOGGER.info("TrainController was created");
    }

    /**
     * Goto trains list page.
     *
     * @param dateStart start of period of time.
     * @param dateEnd   end of period of time.
     * @return view trains.
     */
    @GetMapping(value = "/trains")
    public final String trainsWithFilter(@RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
                                         Model model) {
        LOGGER.info(" IN: trainsWithFilter() - [from: {} to: {}]", dateStart, dateEnd);
        if (!(dateStart == null) && !(dateEnd == null) && dateEnd.isBefore(dateStart)) {
            throw new ValidationErrorException(
                    "The start date {" + dateStart + "} is later than the end date {" + dateEnd + "}.");
        }
        List<TrainDto> trains = trainDtoService.getFilteredByDateTrainListWithPassengersCount(dateStart, dateEnd);
        model.addAttribute("trains", trains);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        LOGGER.info("OUT: trainsWithFilter() - found {} train(s)", trains.size());
        return "trains";
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
     * @return view train.
     */
    @GetMapping(value = "/train")
    public final String gotoAddTrainPage(Model model) {
        LOGGER.info(" IN: gotoAddTrainPage() - []");
        model.addAttribute("isNew", true);
        model.addAttribute("train", new Train());
        LOGGER.info("OUT: gotoAddTrainPage() - [empty train]");
        return "train";
    }

    /**
     * Save new train information into storage. Check name & destination for null and length.
     *
     * @param train new train data.
     * @return view trains.
     */
    @PostMapping(value = "/train")
    public String addTrain(@RequestBody Train train) {
        LOGGER.info(" IN: addTrain() - [{}]", train);
        String errorWithTrainNames = getErrorWithTrainNames(train, "Creation");
        if (errorWithTrainNames != null) {
            throw new ValidationErrorException(errorWithTrainNames);
        }
        Integer newId = trainService.createTrain(train);
        LOGGER.info("OUT: addTrain() - new train id: [{}]", newId);
        return "redirect:/trains";
    }

    /**
     * Update train information in storage. Check name & destination for null and length.
     *
     * @param train updated train data.
     * @return view trains or view error.
     */
    @PostMapping(value = "/train/{id}")
    public String updateTrain(@RequestBody Train train,
                              RedirectAttributes redirectAttributes) {
        LOGGER.info(" IN: updateTrain() - [{}]", train);
        String errorWithTrainNames = getErrorWithTrainNames(train, "Update");
        if (errorWithTrainNames != null) {
            throw new ValidationErrorException(errorWithTrainNames);
        }
        Integer count = trainService.updateTrain(train);
        LOGGER.debug("OUT: updateTrain() - updated: [{}]", count);
        return "redirect:/trains";

    }

    /**
     * Delete train information in storage.
     *
     * @param id train id.
     * @return view trains.
     */
    @GetMapping(value = "/train/{id}/delete")
    public String deleteTrain(@PathVariable Integer id) {
        LOGGER.info(" IN: deleteTrain() - [{}]", id);
        trainService.deleteById(id);
        LOGGER.info("OUT: deleteTrain() - [deleted]");
        return "redirect:/trains";


//        LOGGER.debug("user ask to delete train id: {}", id);
//        Optional<Train> optionalTrain = Optional.of(trainService.findById(id));
//        if (optionalTrain.isPresent()) {
////            if (trainService.isTrainLoaded(id)) {
////                LOGGER.error("...but train id: {} is loaded", id);
////                redirectAttributes.addAttribute(
////                        "errorMessage",
////                        "We're sorry, but we can't delete loaded train. You should remove passenger(s) first.");
////                return "redirect:/error";
////            }
//            LOGGER.debug("execute delete");
//        } else {
//            LOGGER.error("...but train id: {} was not found", id);
//            redirectAttributes.addAttribute(
//                    "errorMessage",
//                    "We're sorry, but we can't find record for delete this train.");
//            return "redirect:/error";
//        }
    }

    private String getErrorWithTrainNames(Train train, String stage) {
        String trainName = train.getTrainName();
        String trainDestination = train.getTrainDestination();
        if (trainName == null) {
            return stage + " failure. The train name cannot be empty.";
        }
        if (trainDestination == null) {
            return stage + " failure. The name of the train's destination cannot be empty.";
        }
        if (trainNameIsOverlong(trainName)) {
            return stage + " failure. The name of the train is too long.";
        }
        if (trainDestinationNameIsOverlong(trainDestination)) {
            return stage + " failure. The name of the train's destination is too long.";
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
