package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.AnimalsRequestDTO;

@Controller
public class AnimalsController {

    String baseUrl = "http://localhost:8080/api/animals";
    RestTemplate restTemplate = new RestTemplate();

    // Find all animals
    @GetMapping("/all")
    public ResponseEntity<?> getAllAnimals(@RequestParam(defaultValue = "BASIC") String detailLevel){
        String url = baseUrl + "?detailLevel=" + detailLevel;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Find by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimalById(@PathVariable Long id,
                                           @RequestParam(defaultValue = "FULL") String detailLevel) {
        String url = baseUrl + "/" + id + "?detailLevel=" + detailLevel;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Add new animal
    @PostMapping("/create")
    public ResponseEntity<?> createAnimal(@RequestBody AnimalsRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AnimalsRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, entity, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Update animal
    @PutMapping("/update")
    public ResponseEntity<?> updateAnimals(@PathVariable Long id, @RequestBody AnimalsRequestDTO request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AnimalsRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, entity, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

    }

    // Delete animal
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id){
        restTemplate.delete(baseUrl + "/" + id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Animal eliminado"));
    }
}
