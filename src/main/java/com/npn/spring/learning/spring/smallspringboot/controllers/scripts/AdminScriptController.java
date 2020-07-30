package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@Controller
public class AdminScriptController {
    @Autowired
    UserServiceInterface userService;

    @GetMapping(value = "/admin/userTables", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getUserTables(Authentication authentication) {
        if (!isAdmin(authentication)) return "{}";

        try {
            return userService.getAllUserAsJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            return "{}";
        }
    }

    private boolean isAdmin(Authentication authentication) {
        User user;
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
        } else {
            return false;
        }
        if (!user.hasRole(UsersRoles.ADMIN_ROLE)) {
            return false;
        }
        return true;
    }

}
