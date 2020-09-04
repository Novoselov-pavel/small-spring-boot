package com.npn.spring.learning.spring.smallspringboot.controllers.scripts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserAuthorityInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.html.HtmlNavElementServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces.ReportServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import com.npn.spring.learning.spring.smallspringboot.model.security.UsersRoles;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @Autowired
    ReportServiceInterface reportService;

    @GetMapping(value = "/admin/userTables", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getUserTables() {
        try {
            return userService.getAllUserAsJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            return "{}";
        }
    }


    @GetMapping(value = "admin/getAllRoles", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getAllRoles() {
        authorityService.getAllAuthorities();
        try {
            return authorityService.getAllAuthoritiesAsJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            return "{}";
        }
    }

    @PutMapping(value = "admin/user/{id}", consumes = {"application/json;charset=UTF-8"}, produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String updateUser(@PathVariable("id") Long id, @RequestBody String body) {
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
        userService.deleteUser(id);
        return "{}";
    }

    @GetMapping(value = "admin/mainMenu", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String getAllHtmlNavElement(){
        try {
            String s = htmlNavElementService.getNavHeaderElementsAsJson();
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Json processing error", e);
        }
    }
    
    @PutMapping(value = "admin/mainMenu/saveNavMenuElement", produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
    public @ResponseBody String saveHtmlNavElement(@RequestBody String body) {
        try {
            htmlNavElementService.saveNavHeaderElementFromJson(body);
            return "{}";
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Json processing error", e);
        }
    }

    @DeleteMapping (value = "/admin/mainMenu/deleteNavMenuElement/{id}", produces = {"application/json;charset=UTF-8"})
    public @ResponseBody String deleteHtmlNavElement(@PathVariable("id") Long id) {
        htmlNavElementService.deleteNavHeaderElement(id);
        return "{}";
    }

    @GetMapping(value = "/admin/report/{reportDocType}/{reportName}", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<Resource> getReport(@PathVariable("reportDocType") String reportDocType,
                                              @PathVariable("reportName") String reportName) {
        try {
            Resource file = reportService.getReport(reportDocType,reportName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            URLEncoder.encode(file.getFilename(), StandardCharsets.UTF_8) + "\"")
                    .body(file);

        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        } catch (IOException e) {
            e.printStackTrace(); // TODO когда будут идеи по логгированию
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e);
        }
    }

}
