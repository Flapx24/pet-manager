package com.example.demo.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api")
public class AuthController {
    
    private final String baseUrl = "http://localhost:8080/api/auth";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(){
        return "register";
    }


    @PostMapping("/login")
    public String login(String email, String password) {

        String url = baseUrl + "/login";

        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(String name, String surname, String password,String confirmPassword, String email) {
        String url = baseUrl + "/register";
    
        Map<String, String> registerData = new HashMap<>();
        registerData.put("name", name);
        registerData.put("surname", surname);
        registerData.put("password", password);
        registerData.put("confirmPassword",confirmPassword);
        registerData.put("email", email);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        HttpEntity<Map<String, String>> request = new HttpEntity<>(registerData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    
        return "redirect:/api/login";
    }

}
