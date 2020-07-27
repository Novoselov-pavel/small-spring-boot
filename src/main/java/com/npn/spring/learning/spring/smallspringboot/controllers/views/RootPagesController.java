package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер для страниц "/","/home","/start", "/registry", "/login"
 */
@Controller
public class RootPagesController {

    @Autowired
    @Qualifier("userService")
    private UserServiceInterface userService;

    /**
     * GET страницы "/","/home"
     * @param model Model
     * @return  "views/BlankPage"если пользотватель не авторизирован, или redirect на страницу пользователя, если пользователь зарегистрирован;
     */
    @GetMapping(value = {"/","/home"})
    public String getHomePage(Model model) {
        insertHomeInBlankPage(model);
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof User) {
            return "redirect:/user/";
        }
        return "views/BlankPage";
    }

    /**
     * GET страницы /registry
     * @param model Model
     * @return "views/BlankPage";
     */
    @GetMapping(value = "/registry")
    public String getRegistryPage(Model model){
        model.addAttribute("user",new User());
        insertRegistryInBlankPage(model);
        return "views/BlankPage";
    }

    /**
     * GET страницы /login
     * @param model Model
     * @return "views/BlankPage";
     */
    @GetMapping(value = "/login")
    public String getLoginPage(Model model) {
        HtmlThymeleafPage pageSettings  = insertLoginInBlankPage(model);
        return "views/BlankPage";
    }

    /**
     * POST страницы /registry. Реализация регистрации пользователя
     *
     * @param user пользователь
     * @param request HttpServletRequest
     * @param model Model
     * @return "redirect:/login" или "views/BlankPage"
     */
    @PostMapping("/registry")
    public String postNewUser(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        try {
            userService.addNewUser(user.getName(),user.getPassword());
            insertLoginInBlankPage(model);
            return "redirect:/login";
        } catch (UserAlreadyExist userAlreadyExist) {
            HtmlThymeleafPage pageSettings = insertRegistryInBlankPage(model);
            pageSettings.setHideMessage(false);
            pageSettings.setMessage("Имя занято, введите другое имя");
            return "views/BlankPage";
        }
    }

    /**
     * Создает дефолтные настройки для домашней страницы "/","/home"
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage insertHomeInBlankPage(Model model) {
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage();
        page.setFragmentName("main");
        page.setHideMessage(false);
        page.setMessage("Выберите действие");
        page.setTitle("Домашняя страница");

        model.addAttribute("hidden", true);
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }

    /**
     * Создает дефолтные настройки для страницы регистрации "/registry"
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage insertRegistryInBlankPage(Model model) {
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage();
        page.setFragmentName("registry");
        page.setHideMessage(true);
        page.setTitle("Регистрация");

        model.addAttribute("hidden", true);
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }

    /**
     * Создает дефолтные настройки для страницы регистрации "/login"
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage insertLoginInBlankPage(Model model) {
        HtmlThymeleafPage page = createDefaultHtmlThymeleafPage();
        page.setFragmentName("login");
        page.setHideMessage(true);
        page.setTitle("Вход");
        model.addAttribute("hidden", true);
        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }


    /**
     * Создает объект для Thymeleaf c дефолтными настройками;
     * @return класс с настройками
     */
    private HtmlThymeleafPage createDefaultHtmlThymeleafPage() {
        HtmlThymeleafPage htmlThymeleafPage = new HtmlThymeleafPage();
        return htmlThymeleafPage;
    }
}
