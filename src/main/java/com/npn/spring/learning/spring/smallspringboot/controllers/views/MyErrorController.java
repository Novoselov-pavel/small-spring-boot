package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер обрабатывающий страницы с ошибками.
 */
@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorTitle = "Error ";
        String errorMessage ="Some problem.";
        if (request!=null) {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (status!=null) {
                int statusCode = Integer.parseInt(status.toString());
                if(statusCode == HttpStatus.NOT_FOUND.value()) {
                    errorMessage = "Not Found.";
                } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    errorMessage = "Server error.";
                } else  if (statusCode == HttpStatus.FORBIDDEN.value()) {
                    errorMessage = "Access denied.";
                }
                errorTitle += status;
            }
        }
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        return "views/Error.html";
    }


    /**
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
