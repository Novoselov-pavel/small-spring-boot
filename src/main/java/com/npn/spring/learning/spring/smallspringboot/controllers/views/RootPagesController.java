package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.exceptions.UserAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootPagesController {

    @Autowired
    @Qualifier("userService")
    private UserServiceInterface userService;


    @GetMapping(value = {"/","/home","/start"})
    public String getHomePage() {
        return "views/Main";
    }

    @GetMapping(value = "/registry")
    public String getRegistryPage(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("hidden", true);
        return "views/Registry";
    }

    @PostMapping("/registry")
    public String postNewUser(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        try {
            userService.addNewUser(user.getName(),user.getPassword());
            model.addAttribute("hidden", false);
            model.addAttribute("notice", "ура");
            return "redirect:/login";
        } catch (UserAlreadyExist userAlreadyExist) {
            model.addAttribute("hidden", false);
            model.addAttribute("notice", "Имя занято, введите другое имя");
            return "views/Registry";
        }
    }
}
