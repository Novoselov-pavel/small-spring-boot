package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElement;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class WorkPageController {

    @Autowired
    HtmlNavElementServiceInterface service;

    /**
     * Открывает страницу пользователя
     *
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping(value = {"/user/","/user"})
    public String getHomePage(Authentication authentication, Model model) {
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
     * Открывает страницу админки
     *
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/admin")
    public String getAdminPage(Authentication authentication, Model model) {
        User user;
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
        } else {
            return "redirect:/login";
        }
        if (!user.hasRole(UsersRoles.ADMIN_ROLE)) {
            return "redirect:/user";
        }
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage(user);
        page.setBodyFragmentRef("views/fragments/AdminBodyFragment.html :: body");
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        model.addAttribute("userTableRef","/admin/userTables");

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
