package by.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @GetMapping(value = "/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
                        Model model) {

//        LOGGER.debug("hello(name:{})", name);
        model.addAttribute("name", name);
        return "hello";
    }
}
