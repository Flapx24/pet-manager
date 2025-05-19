package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.VaccineDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VaccineController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    String baseUrl = "http://localhost:8080/api/animals/";

    @GetMapping("/vaccine/{animalId}/{vaccineId}")
    public String getVaccineById(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId,
            Model model) {

        String url =  baseUrl + animalId + "/vaccines/" + vaccineId;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.path("data");

            VaccineDTO vaccine = objectMapper.treeToValue(dataNode, VaccineDTO.class);
            model.addAttribute("vaccine", vaccine);

            return "vaccine-detail";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo obtener la vacuna");
            return "error";
        }
    }
}
