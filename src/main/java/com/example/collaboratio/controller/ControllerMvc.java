package com.example.collaboratio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerMvc {

    @GetMapping("/")
    public String welcome(){
        return "index";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/mylobby")
    public String mylobby(){
        return "mylobby";
    }

    @GetMapping("/mysessions")
    public String mySessions(){
        return "mysessions";
    }

    @GetMapping("/inside-session")
    public String insideSession(){
        return "inside-session";
    }
}
