package by.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrainsController {

    /**
     * Goto trains list page.
     *
     * @return
     */

    @GetMapping(value = "/trains")
    public final String trains(Model model) {
        return "trains";
    }

    /**
     * Goto edit train page.
     *
     * @return
     */
    @GetMapping(value = "/train/{id}")
    public final String gotoEditTrainPage(@PathVariable Integer id, Model model) {
        return "train";
    }

    /**
     * Goto add train page.
     *
     * @return
     */
    @GetMapping(value = "/train/add")
    public final String gotoAddTrainPage(Model model) {
        return "train";
    }
}
