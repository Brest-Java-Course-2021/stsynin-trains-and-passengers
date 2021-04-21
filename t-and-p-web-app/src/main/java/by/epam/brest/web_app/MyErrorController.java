package by.epam.brest.web_app;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Any error controller.
 */
@Controller
public class MyErrorController implements ErrorController {

    /**
     * Custom error pages show.
     *
     * @param request      HttpServletRequest.
     * @param errorMessage error message string.
     * @return error view.
     */
    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request,
                                    @RequestParam(required = false) String errorMessage) {
        ModelAndView errorPage = new ModelAndView("error");
        if (errorMessage != null) {
            errorPage.addObject("errorMessage", errorMessage);
            return errorPage;
        }
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorPage.addObject("errorMessage", getErrorMessage(statusCode));
        }
        return errorPage;
    }

    private String getErrorMessage(int statusCode) {
        String errorMsg = "";
        switch (statusCode) {
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        return errorMsg;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}