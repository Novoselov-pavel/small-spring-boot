package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.html.services.HtmlNavElementService;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class WorkPageController {

    @Autowired
    HtmlNavElementService service;

    @GetMapping(value = {"/user/","/user"})
    public String getHomePage(Authentication authentication, Model model) {
//        Authentication authentication
        User user;
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
        } else {
            return "redirect:/login";
        }
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage(user);
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);


        return "views/StandardPageTemplate.html";
    }


    /**
     * Создает объект для Thymeleaf c дефолтными настройками;
     * @return класс с настройками
     */
    private HtmlThymeleafPage createDefaultHtmlThymeleafPage(User user) {
        HtmlThymeleafPage htmlThymeleafPage = new HtmlThymeleafPage();
        htmlThymeleafPage.setNavName("mainNav");
        htmlThymeleafPage.setNavElements(service.getNavHeaderElements(user));
        return htmlThymeleafPage;
    }

}
