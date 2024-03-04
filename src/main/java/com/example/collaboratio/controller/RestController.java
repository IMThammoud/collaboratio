package com.example.collaboratio.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @GetMapping("/get-info")
    public String getInfo(){

        return "this info was queried via htmx";
    }
}
