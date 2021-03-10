package by.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PassengersController {

    /**
     * Goto passengers list page.
     *
     * @return
     */

    @GetMapping(value = "/passengers")
    public final String passengers(Model model) {
        return "passengers";
    }

    /**
     * Goto edit passenger page.
     *
     * @return
     */
    @GetMapping(value = "/passenger/{id}")
    public final String gotoEditPassengerPage(@PathVariable Integer id, Model model) {
        model.addAttribute("mode", "edit");
        return "passenger";
    }

    /**
     * Goto add passenger page.
     *
     * @return
     */
    @GetMapping(value = "/passenger/add")
    public final String gotoAddPassengerPage(Model model) {
        model.addAttribute("mode", "add new");
        return "passenger";
    }
}
