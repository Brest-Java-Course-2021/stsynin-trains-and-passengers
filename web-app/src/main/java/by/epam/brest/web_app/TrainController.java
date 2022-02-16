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

import javax.validation.Valid;
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
    public String addTrain(@Valid @RequestBody Train train) {
        LOGGER.info(" IN: addTrain() - [{}]", train);
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
    public String updateTrain(@Valid @RequestBody Train train) {
        LOGGER.info(" IN: updateTrain() - [{}]", train);
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
    }

    private boolean trainNameIsOverlong(String name) {
        return name.length() > MAX_TRAIN_NAME_LENGTH;
    }

    private boolean trainDestinationNameIsOverlong(String destination) {
        return destination.length() > MAX_TRAIN_DESTINATION_NAME_LENGTH;
    }
}
