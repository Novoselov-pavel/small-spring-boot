package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Контроллер для работы с информацией администратора
 */
@Controller
@Secured("ADMIN_ROLE")
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

    /**
     * Эта функция в общем излишняя, так как у нас есть @Secured("ADMIN_ROLE").
     * Но, так как эта работа с критически важной информацией, мы перестрахуемся.
     *
     * @param authentication Authentication
     * @return true если пользователь в группе администраторы, иначе false.
     */
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
