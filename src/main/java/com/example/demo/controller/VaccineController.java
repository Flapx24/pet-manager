package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.VaccineDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class VaccineController {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper;

    String baseUrl = "http://localhost:8080/api/animals/";

    @GetMapping("/animals/{animalId}/vaccines")
    public String showVaccines(
        @PathVariable Long animalId,
        HttpSession session,
        Model model) {

        try {
            String token = (String) session.getAttribute("token");

            if (token == null) {
                model.addAttribute("error", "Token no encontrado en sesión");
                return "error";
            }
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + animalId + "/vaccines")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .queryParam("sortBy", "applicationDate")
                    .queryParam("direction", "DESC")
                    .queryParam("dateType", "application")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.path("data");
            JsonNode vaccinesNode = dataNode.path("vaccines");

            List<VaccineDTO> vaccines = new ArrayList<>();
            for (JsonNode node : vaccinesNode) {
                VaccineDTO vaccine = objectMapper.treeToValue(node, VaccineDTO.class);
                vaccines.add(vaccine);
            }

            model.addAttribute("vaccines", vaccines);
            model.addAttribute("pageInfo", dataNode);

            return "historyVaccine";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudieron obtener las vacunas");
            return "error";
        }
    }


    // Get vaccine by id
    @GetMapping("/{animalId}/vaccine/{vaccineId}")
    public String getVaccineById(
        @PathVariable Long animalId,
        @PathVariable Long vaccineId,
        Model model) {

        String url = baseUrl +  animalId + "/vaccines" + vaccineId;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.path("data");

            VaccineDTO vaccine = objectMapper.treeToValue(dataNode, VaccineDTO.class);
            model.addAttribute("vaccine", vaccine);

            return "historyVaccine";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo obtener la vacuna");
            return "error";
        }
    }

    // Creation of a vaccine for a specific animal
    @PostMapping("/animals/{animalId}/vaccines")
    public String createVaccineForAnimal(@PathVariable Long animalId,
                                     @ModelAttribute VaccineDTO vaccineDTO,
                                     @RequestParam("token") String token,
                                     Model model) {
        try {
            String url = baseUrl + animalId + "/vaccines";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);  // usa el token recibido como parámetro

            HttpEntity<VaccineDTO> requestEntity = new HttpEntity<>(vaccineDTO, headers);

            restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                VaccineDTO.class
            );

            return "redirect:/mascotas";

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear vacuna: " + e.getMessage());
            return "formVaccine";
        }
    }


    @PutMapping("/animals/{animalId}/vaccines/{vaccineId}")
    public ResponseEntity<?> updateVaccine(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId,
            @RequestBody VaccineDTO vaccineDTO,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            String url = baseUrl + animalId + "/vaccines/" + vaccineId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<VaccineDTO> requestEntity = new HttpEntity<>(vaccineDTO, headers);
            ResponseEntity<VaccineDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                VaccineDTO.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar vacuna: " + e.getMessage());
        }
    }

    @PatchMapping("/confirm-vaccine/{animalId}/{vaccineId}")
    public ResponseEntity<?> confirmVaccineApplication(
            @PathVariable Long animalId,
            @PathVariable Long vaccineId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            String url = baseUrl + animalId + "/vaccines/" + vaccineId + "/apply";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error desde API externa: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al confirmar aplicación de la vacuna: " + e.getMessage());
        }
    }

     @GetMapping("/vaccines/non-expired/{animalId}")
    public String getNonExpiredVaccines(
            @PathVariable Long animalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "expirationDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestHeader("Authorization") String authHeader,
            Model model) {

        try {
            String token = authHeader.replace("Bearer ", "");
            String url = baseUrl + animalId + "/vaccines/non-expired"
                    + "?page=" + page
                    + "&size=" + size
                    + "&sortBy=" + sortBy
                    + "&direction=" + direction;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.path("data");
            JsonNode vaccinesNode = dataNode.path("vaccines");

            List<JsonNode> vaccines = new ArrayList<>();
            vaccinesNode.forEach(vaccines::add);

            model.addAttribute("vaccines", vaccines);
            model.addAttribute("pageInfo", dataNode);

            return "non-expired-vaccines";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudieron obtener las vacunas no expiradas");
            return "error";
        }
    }
}
