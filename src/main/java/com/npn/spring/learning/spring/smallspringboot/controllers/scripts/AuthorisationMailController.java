package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlThymeleafPage;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.AuthorisationEmailExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер авторизации email адреса
 */
@Controller
public class AuthorisationMailController {


    @Autowired
    AuthorisationMailDataInterface service;

    @GetMapping("/authorisationMail")
    public String confirmAuthorisation(@RequestParam(name = "userNameToken") String userNameToken,
                                     @RequestParam (name = "authorisationToken") String authorisationToken, Model model) {
        try{
            if (service.confirmAuthorisation(userNameToken, authorisationToken)){
                getAcceptPage(model);
                return "views/BlankPage";
            } else {
                getFailPage(model);
                return "views/BlankPage";
            }
        } catch (AuthorisationEmailExpired authorisationEmailExpired) {
            getAuthorisationEmailExpired(model);
            model.addAttribute("href",getMailResendHref(userNameToken,authorisationToken));
            return "views/BlankPage";
        }
    }

    @GetMapping("/authorisationMail/resendMail")
    public String resendMail(@RequestParam(name = "userNameToken") String userNameToken,
                             @RequestParam(name = "authorisationToken") String authorisationToken, Model model){

        service.delete(userNameToken, authorisationToken);
        getResendMailPage(model);
        return "views/BlankPage";
    }


    /**
     * Создает страницу подтвержения регистрации
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage getResendMailPage(Model model) {
        HtmlThymeleafPage page = new HtmlThymeleafPage();
        page.setFragmentName("failMessage");
        page.setHideMessage(true);
        page.setTitle("Письмо направлено повторно");

        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }

    private String getMailResendHref(String userNameToken, String authorisationToken) {
        return "/authorisationMail/resendMail?userNameToken=" + userNameToken + "&authorisationToken="+authorisationToken;
    }

    /**
     * Создает страницу подтвержения регистрации
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage getAcceptPage(Model model) {
        HtmlThymeleafPage page = new HtmlThymeleafPage();
        page.setFragmentName("acceptMessage");
        page.setHideMessage(true);
        page.setTitle("Регистрация подтверждена");

        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }

    /**
     * Создает страницу отказа регистрации
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage getFailPage(Model model) {
        HtmlThymeleafPage page = new HtmlThymeleafPage();
        page.setFragmentName("failMessage");
        page.setHideMessage(true);
        page.setTitle("Регистрация не подтверждена");

        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }

    /**
     * Создает страницу истечения срока жизни письма регистрации
     * @param model модель передающаяся в страницу
     * @return класс с настройками
     */
    private HtmlThymeleafPage getAuthorisationEmailExpired(Model model) {
        HtmlThymeleafPage page = new HtmlThymeleafPage();
        page.setFragmentName("authorisationEmailExpired");
        page.setHideMessage(true);
        page.setTitle("Срок действия авторизационного письма истек.");


        model.addAttribute(HtmlThymeleafPage.getThymeleafObjectName(),page);
        return page;
    }
}
