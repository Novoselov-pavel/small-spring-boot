package com.npn.spring.learning.spring.smallspringboot.controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootPagesController {
    @GetMapping(value = {"/","/home","/start"})
    public String getHomePage() {
        return "views/Main";
    }
}
