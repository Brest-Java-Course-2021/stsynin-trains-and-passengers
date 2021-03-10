package by.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainsController {

    @GetMapping(value = "/trains")
    public String defaultPageRedirect() {
        return "trains";
    }

}
