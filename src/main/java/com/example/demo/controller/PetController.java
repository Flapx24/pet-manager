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

        if (mascotaDTO.getRaza() != null && !mascotaDTO.getRaza().isEmpty()) {
            requestBody.put("breed", mascotaDTO.getRaza());
        }

        switch (mascotaDTO.getTipoAnimal()) {
            case DOG:
                if (mascotaDTO.getTamano() != null)
                    requestBody.put("size", mascotaDTO.getTamano());
                if (mascotaDTO.getTipoPelaje() != null)
                    requestBody.put("coatType", mascotaDTO.getTipoPelaje());
                requestBody.put("pedigree", mascotaDTO.isPedigree());
                break;
            case CAT:
                if (mascotaDTO.getTipoPelaje() != null)
                    requestBody.put("coatType", mascotaDTO.getTipoPelaje());
                requestBody.put("indoorOnly", mascotaDTO.isIndoor());
                break;
            case BIRD:
                if (mascotaDTO.getEspecie() != null)
                    requestBody.put("species", mascotaDTO.getEspecie());
                requestBody.put("clippedWings", mascotaDTO.isAlasCortadas());
                requestBody.put("talkingAbility", mascotaDTO.isHabla());
                break;
            case REPTILE:
                if (mascotaDTO.getEspecie() != null)
                    requestBody.put("species", mascotaDTO.getEspecie());
                if (mascotaDTO.getTipoHabitat() != null)
                    requestBody.put("habitatType", mascotaDTO.getTipoHabitat());
                if (mascotaDTO.getRequisitosTemperatura() != null)
                    requestBody.put("temperatureRequirements", mascotaDTO.getRequisitosTemperatura());
                requestBody.put("venomous", mascotaDTO.isVenenoso());
                break;
            case FISH:
                if (mascotaDTO.getTipoAgua() != null)
                    requestBody.put("waterType", mascotaDTO.getTipoAgua());
                if (mascotaDTO.getTemperaturaAgua() != null)
                    requestBody.put("waterTemperature", mascotaDTO.getTemperaturaAgua());
                if (mascotaDTO.getNivelPH() != null)
                    requestBody.put("phLevel", mascotaDTO.getNivelPH());
                if (mascotaDTO.getComportamientoSocial() != null)
                    requestBody.put("socialBehavior", mascotaDTO.getComportamientoSocial());
                break;
            case RODENT:
                if (mascotaDTO.getEspecie() != null)
                    requestBody.put("species", mascotaDTO.getEspecie());
                requestBody.put("cageTrained", mascotaDTO.isEntrenadoJaula());
                if (mascotaDTO.getEsperanzaVida() != null)
                    requestBody.put("lifespanYears", mascotaDTO.getEsperanzaVida());
                if (mascotaDTO.getCondicionDientes() != null)
                    requestBody.put("teethCondition", mascotaDTO.getCondicionDientes());
                break;
            default:
                break;
        }

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
        mascota.setEsterilizado(safeGetBoolean(mascotaApi, "sterilized"));
        mascota.setUltimaDesparasitacion(mascotaApi.get("lastDewormingDate") != null
                ? LocalDate.parse(mascotaApi.get("lastDewormingDate").toString())
                : null);

        mascota.setRaza((String) mascotaApi.get("breed"));

        switch (mascota.getTipoAnimal()) {
            case DOG:
                mascota.setTamano((String) mascotaApi.get("size"));
                mascota.setTipoPelaje((String) mascotaApi.get("coatType"));
                mascota.setPedigree(safeGetBoolean(mascotaApi, "pedigree"));
                break;
            case CAT:
                mascota.setTipoPelaje((String) mascotaApi.get("coatType"));
                mascota.setIndoor(safeGetBoolean(mascotaApi, "indoorOnly"));
                break;
            case BIRD:
                mascota.setEspecie((String) mascotaApi.get("species"));
                mascota.setAlasCortadas(safeGetBoolean(mascotaApi, "clippedWings"));
                mascota.setHabla(safeGetBoolean(mascotaApi, "talkingAbility"));
                break;
            case REPTILE:
                mascota.setEspecie((String) mascotaApi.get("species"));
                mascota.setTipoHabitat((String) mascotaApi.get("habitatType"));
                mascota.setRequisitosTemperatura((String) mascotaApi.get("temperatureRequirements"));
                mascota.setVenenoso(safeGetBoolean(mascotaApi, "venomous"));
                break;
            case FISH:
                mascota.setTipoAgua((String) mascotaApi.get("waterType"));
                mascota.setTemperaturaAgua(safeGetDouble(mascotaApi, "waterTemperature"));
                mascota.setNivelPH(safeGetDouble(mascotaApi, "phLevel"));
                mascota.setComportamientoSocial((String) mascotaApi.get("socialBehavior"));
                break;
            case RODENT:
                mascota.setEspecie((String) mascotaApi.get("species"));
                mascota.setEntrenadoJaula(safeGetBoolean(mascotaApi, "cageTrained"));
                mascota.setEsperanzaVida(safeGetInteger(mascotaApi, "lifespanYears"));
                mascota.setCondicionDientes((String) mascotaApi.get("teethCondition"));
                break;
            default:
                break;
        }

        return mascota;
    }

    private Boolean safeGetBoolean(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            }
        }
        return false;
    }

    private Double safeGetDouble(Map<String, Object> map, String key) {
        if (map.containsKey(key) && map.get(key) != null) {
            try {
                return Double.parseDouble(map.get(key).toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Integer safeGetInteger(Map<String, Object> map, String key) {
        if (map.containsKey(key) && map.get(key) != null) {
            try {
                return Integer.parseInt(map.get(key).toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}