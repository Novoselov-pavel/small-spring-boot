package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.html.services.HtmlNavElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class WorkPageController {

    @Autowired
    HtmlNavElementService service;

    @GetMapping("/user/")
    public String getHomePage(@AuthenticationPrincipal Principal principal, Model model) {
        List<HtmlNavElement> list = service.getNavHeaderElements();

        return "views/Main";
    }

    /**
     * Создает объект для Thymeleaf c дефолтными настройками;
     * @return класс с настройками
     */
    private HtmlThymeleafPage createDefaultHtmlThymeleafPage() {
        HtmlThymeleafPage htmlThymeleafPage = new HtmlThymeleafPage();
        htmlThymeleafPage.setNavName("mainNav");
        return htmlThymeleafPage;
    }
}
