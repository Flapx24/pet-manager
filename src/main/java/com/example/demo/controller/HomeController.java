package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.VaccineDTO;

import jakarta.servlet.http.HttpSession;


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
        return "vaccine";
    }

    @GetMapping("/animals/{animalId}/vaccines/form")
    public String formVaccine(@PathVariable Long animalId, Model model, HttpSession session) {
        model.addAttribute("animalId", animalId);
        model.addAttribute("vaccineDTO", new VaccineDTO());

        String token = (String) session.getAttribute("token");
        model.addAttribute("token", token);

        return "formVaccine";
    }

    
}
