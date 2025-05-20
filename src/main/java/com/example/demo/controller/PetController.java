package com.example.demo.controller;

import com.example.demo.dto.MascotaDTO;
import com.example.demo.enums.AnimalType;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mascotas")
public class PetController {

    private final String baseUrl = "http://localhost:8080/api/animals";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String listarMascotas(Model model, HttpSession session,
            @RequestParam(defaultValue = "FULL") String detailLevel) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/api/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "?detailLevel=" + detailLevel,
                    HttpMethod.GET,
                    request,
                    Map.class);

            if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody().get("success"))) {
                List<Map<String, Object>> mascotasApi = (List<Map<String, Object>>) response.getBody().get("data");
                List<MascotaDTO> mascotas = mascotasApi.stream()
                        .map(this::convertToMascotaDTO)
                        .toList();

                model.addAttribute("mascotas", mascotas);
                model.addAttribute("tiposAnimal", AnimalType.values());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener las mascotas: " + e.getMessage());
        }

        return "list";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaMascota(Model model) {
        model.addAttribute("mascota", new MascotaDTO());
        model.addAttribute("tiposAnimal", AnimalType.values());
        return "form";
    }

    @PostMapping
    public String crearMascota(@ModelAttribute MascotaDTO mascotaDTO, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/api/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = buildRequestBody(mascotaDTO);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);
            if (response.getStatusCode() != HttpStatus.CREATED
                    || !Boolean.TRUE.equals(response.getBody().get("success"))) {
                return "redirect:/mascotas?error=create";
            }
        } catch (Exception e) {
            return "redirect:/mascotas?error=create";
        }

        return "redirect:/mascotas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMascota(@PathVariable Long id, Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/api/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "/" + id + "?detailLevel=FULL",
                    HttpMethod.GET,
                    request,
                    Map.class);

            if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody().get("success"))) {
                Map<String, Object> mascotaApi = (Map<String, Object>) response.getBody().get("data");
                MascotaDTO mascota = convertToMascotaDTO(mascotaApi);
                model.addAttribute("mascota", mascota);
                model.addAttribute("tiposAnimal", AnimalType.values());
                return "form";
            }
        } catch (Exception e) {
            return "redirect:/mascotas?error=edit";
        }

        return "redirect:/mascotas";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarMascota(@PathVariable Long id, @ModelAttribute MascotaDTO mascotaDTO, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/api/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = buildRequestBody(mascotaDTO);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.exchange(
                    baseUrl + "/" + id,
                    HttpMethod.PUT,
                    request,
                    Void.class);
        } catch (Exception e) {
            return "redirect:/mascotas?error=update";
        }

        return "redirect:/mascotas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/api/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    baseUrl + "/" + id,
                    HttpMethod.DELETE,
                    request,
                    Void.class);
        } catch (Exception e) {
            return "redirect:/mascotas?error=delete";
        }

        return "redirect:/mascotas";
    }

    private Map<String, Object> buildRequestBody(MascotaDTO mascotaDTO) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", mascotaDTO.getNombre());
        requestBody.put("birthDate", mascotaDTO.getFechaNacimiento());
        requestBody.put("weightKg", mascotaDTO.getPeso());
        requestBody.put("gender", mascotaDTO.getSexo());
        requestBody.put("animalType", mascotaDTO.getTipoAnimal());
        requestBody.put("notes", mascotaDTO.getNotas());
        requestBody.put("diet", mascotaDTO.getDieta());
        requestBody.put("sterilized", mascotaDTO.isEsterilizado());
        requestBody.put("lastDewormingDate", mascotaDTO.getUltimaDesparasitacion());
        requestBody.put("color", mascotaDTO.getColor());

        Map<String, Object> specificFields = new HashMap<>();
        specificFields.put("breed", mascotaDTO.getRaza());

        switch (mascotaDTO.getTipoAnimal()) {
            case DOG:
                specificFields.put("size", mascotaDTO.getTamano());
                specificFields.put("coatType", mascotaDTO.getTipoPelaje());
                specificFields.put("pedigree", mascotaDTO.isPedigree());
                break;
            case CAT:
                specificFields.put("coatType", mascotaDTO.getTipoPelaje());
                specificFields.put("indoor", mascotaDTO.isIndoor());
                break;
            case BIRD:
                specificFields.put("species", mascotaDTO.getEspecie());
                specificFields.put("wingsClipped", mascotaDTO.isAlasCortadas());
                specificFields.put("talks", mascotaDTO.isHabla());
                break;
            case REPTILE:
                specificFields.put("species", mascotaDTO.getEspecie());
                specificFields.put("habitatType", mascotaDTO.getTipoHabitat());
                specificFields.put("temperatureRequirements", mascotaDTO.getRequisitosTemperatura());
                specificFields.put("venomous", mascotaDTO.isVenenoso());
                break;
            case FISH:
                specificFields.put("waterType", mascotaDTO.getTipoAgua());
                specificFields.put("waterTemperature", mascotaDTO.getTemperaturaAgua());
                specificFields.put("phLevel", mascotaDTO.getNivelPH());
                specificFields.put("socialBehavior", mascotaDTO.getComportamientoSocial());
                break;
            case RODENT:
                specificFields.put("species", mascotaDTO.getEspecie());
                specificFields.put("cageTrained", mascotaDTO.isEntrenadoJaula());
                specificFields.put("lifeExpectancy", mascotaDTO.getEsperanzaVida());
                specificFields.put("teethCondition", mascotaDTO.getCondicionDientes());
                break;
            default:
                break;
        }

        requestBody.put("specificFields", specificFields);
        return requestBody;
    }

    private MascotaDTO convertToMascotaDTO(Map<String, Object> mascotaApi) {
        MascotaDTO mascota = new MascotaDTO();

        mascota.setId(Long.valueOf(mascotaApi.get("id").toString()));
        mascota.setNombre((String) mascotaApi.get("name"));
        mascota.setFechaNacimiento(
                mascotaApi.get("birthDate") != null ? LocalDate.parse(mascotaApi.get("birthDate").toString()) : null);
        mascota.setFechaRegistro(mascotaApi.get("registrationDate") != null
                ? LocalDate.parse(mascotaApi.get("registrationDate").toString())
                : null);
        mascota.setPeso(
                mascotaApi.get("weightKg") != null ? Double.parseDouble(mascotaApi.get("weightKg").toString()) : null);
        mascota.setColor((String) mascotaApi.get("color"));
        mascota.setSexo((String) mascotaApi.get("gender"));
        mascota.setTipoAnimal(
                mascotaApi.get("animalType") != null ? AnimalType.valueOf(mascotaApi.get("animalType").toString())
                        : null);
        mascota.setNotas((String) mascotaApi.get("notes"));
        mascota.setDieta((String) mascotaApi.get("diet"));
        mascota.setEsterilizado(mascotaApi.get("sterilized") != null &&
                Boolean.parseBoolean(mascotaApi.get("sterilized").toString()));
        mascota.setUltimaDesparasitacion(mascotaApi.get("lastDewormingDate") != null
                ? LocalDate.parse(mascotaApi.get("lastDewormingDate").toString())
                : null);

        if (mascotaApi.get("specificFields") != null) {
            Map<String, Object> specificFields = (Map<String, Object>) mascotaApi.get("specificFields");

            mascota.setRaza((String) specificFields.get("breed"));

            switch (mascota.getTipoAnimal()) {
                case DOG:
                    mascota.setTamano((String) specificFields.get("size"));
                    mascota.setTipoPelaje((String) specificFields.get("coatType"));
                    mascota.setPedigree(specificFields.get("pedigree") != null &&
                            Boolean.parseBoolean(specificFields.get("pedigree").toString()));
                    break;
                case CAT:
                    mascota.setTipoPelaje((String) specificFields.get("coatType"));
                    mascota.setIndoor(specificFields.get("indoor") != null &&
                            Boolean.parseBoolean(specificFields.get("indoor").toString()));
                    break;
                case BIRD:
                    mascota.setEspecie((String) specificFields.get("species"));
                    mascota.setAlasCortadas(specificFields.get("wingsClipped") != null &&
                            Boolean.parseBoolean(specificFields.get("wingsClipped").toString()));
                    mascota.setHabla(specificFields.get("talks") != null &&
                            Boolean.parseBoolean(specificFields.get("talks").toString()));
                    break;
                case REPTILE:
                    mascota.setEspecie((String) specificFields.get("species"));
                    mascota.setTipoHabitat((String) specificFields.get("habitatType"));
                    mascota.setRequisitosTemperatura((String) specificFields.get("temperatureRequirements"));
                    mascota.setVenenoso(specificFields.get("venomous") != null &&
                            Boolean.parseBoolean(specificFields.get("venomous").toString()));
                    break;
                case FISH:
                    mascota.setTipoAgua((String) specificFields.get("waterType"));
                    mascota.setTemperaturaAgua(specificFields.get("waterTemperature") != null
                            ? Double.parseDouble(specificFields.get("waterTemperature").toString())
                            : null);
                    mascota.setNivelPH(specificFields.get("phLevel") != null
                            ? Double.parseDouble(specificFields.get("phLevel").toString())
                            : null);
                    mascota.setComportamientoSocial((String) specificFields.get("socialBehavior"));
                    break;
                case RODENT:
                    mascota.setEspecie((String) specificFields.get("species"));
                    mascota.setEntrenadoJaula(specificFields.get("cageTrained") != null &&
                            Boolean.parseBoolean(specificFields.get("cageTrained").toString()));
                    mascota.setEsperanzaVida(specificFields.get("lifeExpectancy") != null
                            ? Integer.parseInt(specificFields.get("lifeExpectancy").toString())
                            : null);
                    mascota.setCondicionDientes((String) specificFields.get("teethCondition"));
                    break;
                default:
                    break;
            }
        }

        return mascota;
    }
}