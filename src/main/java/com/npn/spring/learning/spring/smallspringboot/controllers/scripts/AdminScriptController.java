package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для работы с информацией администратора
 */
@Controller
@Secured("ROLE_ADMIN")
public class AdminScriptController {
    @Autowired
    UserServiceInterface userService;

    @Autowired
    UserAuthorityInterface authorityService;

    @Autowired
    HtmlNavElementServiceInterface htmlNavElementService;

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


    @GetMapping(value = "admin/getAllRoles", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getAllRoles(Authentication authentication) {
        if (!isAdmin(authentication)) return "{}";
        authorityService.getAllAuthorities();
        try {
            return authorityService.getAllAuthoritiesAsJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            return "{}";
        }
    }

    @PutMapping(value = "admin/user/{id}", consumes = {"application/json;charset=UTF-8"}, produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String updateUser(@PathVariable("id") Long id, Authentication authentication, @RequestBody String body) {
        if (!isAdmin(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden");
        }
        try {
            User user = userService.updateUser(id,body);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
            }
        } catch (ParseException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad Json object");
        }
        return "{}";
    }

    @DeleteMapping(value = "admin/user/{id}",  produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String deleteUser(@PathVariable("id") Long id, Authentication authentication) {
        if (!isAdmin(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden");
        }
        userService.deleteUser(id);
        return "{}";
    }

    @GetMapping(value = "admin/mainMenu", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getAllHtmlNavElement(Authentication authentication){
        if (!isAdmin(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden");
        }
        try {
            String s = htmlNavElementService.getNavHeaderElementsAsJson();
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Json processing error", e);
        }
    }
    
    @PutMapping(value = "admin/mainMenu/saveNavMenuElement", produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
    public @ResponseBody String saveHtmlNavElement(Authentication authentication, @RequestBody String body) {
        if (!isAdmin(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden");
        }
        try {
            htmlNavElementService.saveNavHeaderElementFromJson(body);
            return "{}";
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Json processing error", e);
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
        if (!user.hasRole(UsersRoles.ROLE_ADMIN)) {
            return false;
        }
        return true;
    }
    
}
