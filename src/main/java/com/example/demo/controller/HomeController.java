package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/api/login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/vaccine")
    public String vaccine() {
        return "historyVaccine";
    }
}
