package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class AuthController {

    private final String baseUrl = "http://localhost:8080/api/auth";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(required = false) String error,
            @RequestParam(required = false) String registered,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas.");
        }
        if (registered != null) {
            model.addAttribute("registered", "Registro exitoso. Ahora puedes iniciar sesión.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String mostrarRegistro() {
        return "register";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        String url = baseUrl + "/login";

        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (Boolean.TRUE.equals(response.getBody().get("success"))) {
                String token = (String) response.getBody().get("token");
                String name = (String) response.getBody().get("name");

                session.setAttribute("token", token);
                session.setAttribute("name", name);

                return "redirect:/index";
            } else {
                return "redirect:/api/login?error=true";
            }

        } catch (Exception e) {
            return "redirect:/api/login?error=true";
        }
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/api/register?error=nomatch";
        }

        String url = baseUrl + "/register";

        Map<String, String> registerData = new HashMap<>();
        registerData.put("name", name);
        registerData.put("surname", surname);
        registerData.put("email", email);
        registerData.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(registerData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (Boolean.TRUE.equals(response.getBody().get("success"))) {
                return "redirect:/api/login?registered=true";
            } else {
                return "redirect:/api/register?error=email";
            }

        } catch (Exception e) {
            return "redirect:/api/register?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/api/login";
    }
}
