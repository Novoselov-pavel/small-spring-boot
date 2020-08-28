package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Контроллер для страниц "/","/home","/start", "/registry", "/login"
 */
@Controller
public class RootPagesController {

    /**
     * Базовая часть дефолтного адреса для логина через oauth2
     */
    private static final String authorizationRequestBaseUri = "oauth2/authorization";

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

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
        Collection<GrantedAuthority> collection = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (collection.stream().anyMatch(x->UsersRoles.containRole(x.getAuthority()))) {
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
    public String getLoginPage(@RequestParam(name = "error", required = false, defaultValue = "false") boolean isError,  Model model) {
        HtmlThymeleafPage pageSettings  = insertLoginInBlankPage(model);
        if (isError) {
            pageSettings.setHideMessage(false);
            pageSettings.setMessage("Login error");
        }
        model.addAttribute("oaths",getOauth2AuthenticationUrls());
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

    /**
     * Получает список всекхъ служб OAuth2 которые зарегистрированы в при
     * @return
     */
    private Map<String,String> getOauth2AuthenticationUrls() {
        Map<String, String> retVal = new HashMap<>();
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        if (clientRegistrations == null) return retVal;
        clientRegistrations.forEach(registration ->
                retVal.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        return  retVal;
    }

}
