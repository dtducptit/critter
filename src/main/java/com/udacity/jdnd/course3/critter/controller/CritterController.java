package com.udacity.jdnd.course3.critter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CritterController {

    @GetMapping("/test")
    public String test() {
        return "Critter Starter installed successfully";
    }
}