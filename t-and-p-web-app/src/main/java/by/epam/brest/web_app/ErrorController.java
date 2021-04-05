package by.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Any error controller.
 */
@Controller
public class ErrorController {

    /**
     * Default error page.
     *
     * @param model model.
     * @return view error.
     */
    @GetMapping(value = "/error")
    public String errorPage(
            @RequestParam("errorMessage") String errorMessage,
            Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}