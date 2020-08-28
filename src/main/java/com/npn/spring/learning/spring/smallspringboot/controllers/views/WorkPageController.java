package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;


/**
 * Контроллер для получения страниц "/user/","/user", "/admin"
 */
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
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping(value = {"/user/","/user"})
    public String getHomePage(Authentication authentication, Model model) {
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage(authentication.getAuthorities());
        page.setBodyFragmentRef("views/fragments/UserBodyMainFragment.html :: userMain");
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
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String getAdminPage(Authentication authentication, Model model) {
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage(authentication.getAuthorities());
        page.setBodyFragmentRef("views/fragments/AdminBodyFragment.html :: body");
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        model.addAttribute("userTableRef","/admin/userTables");

        return "views/StandardPageTemplate.html";
    }


    /**
     * Создает объект для Thymeleaf c дефолтными настройками;
     * @return класс с настройками
     */
    private HtmlThymeleafPage createDefaultHtmlThymeleafPage(final Collection<? extends GrantedAuthority> user) {
        HtmlThymeleafPage htmlThymeleafPage = new HtmlThymeleafPage();
        htmlThymeleafPage.setNavName("mainNav");
        htmlThymeleafPage.setNavElements(service.getNavHeaderElements(user));
        return htmlThymeleafPage;
    }

}
