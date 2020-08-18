package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.AuthorisationEmailExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Контроллер авторизации email адреса
 */
@Controller
public class AuthorisationMailController {

    @Autowired
    AuthorisationMailDataInterface service;

    @GetMapping("/authorisationMail")
    public String confirmAuthorisation(@PathVariable("userNameToken") String userNameToken,
                                     @PathVariable ("authorisationToken") String authorisationToken) {
        try{
            //TODO
            if (service.confirmAuthorisation(userNameToken, authorisationToken)){
                return null;
            } else {
                return null;
            }
        } catch (AuthorisationEmailExpired authorisationEmailExpired) {
            return null;
        }
    }

}
