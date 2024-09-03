package com.example.university.jobProfiles;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobProfileService {

    private final RestTemplate restTemplate;

    public JobProfileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, List<String>> getRandomResponsibilities(List<String> faculties, int numResponsibilities) {
        String url = "http://127.0.0.1:5000/random-responsibilities";
        Map<String, Object> request = new HashMap<>();
        request.put("faculties", faculties);
        request.put("num_responsibilities", numResponsibilities);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(url, entity, Map.class);
    }

    public Map<String, List<String>> getRandomSkills(List<String> faculties, int numSkills) {
        String url = "http://127.0.0.1:5000/random-skills";
        Map<String, Object> request = new HashMap<>();
        request.put("faculties", faculties);
        request.put("num_skills", numSkills);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(url, entity, Map.class);
    }

    public String preprocessAndPredict(String text) {
        String url = "http://127.0.0.1:5000/predict";
        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(url, entity, String.class);
    }
    private static final String RECOMMEND_URL = "http://127.0.0.1:5000/recommend";
    public List<String> getRecommendations(String faculty) {
        RestTemplate restTemplate = new RestTemplate();


        // Postavljanje zaglavlja
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kreiranje HttpEntitiy objekta
        HttpEntity<String> requestEntity = new HttpEntity<>(faculty, headers);

        // Slanje POST zahteva
        ResponseEntity<RecommendationResponse> response = restTemplate.exchange(
                RECOMMEND_URL,
                HttpMethod.POST,
                requestEntity,
                RecommendationResponse.class
        );

        // Proverite status koda i obradite odgovor
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getRecommendations();
        } else {
            throw new RuntimeException("Failed to get recommendations: " + response.getStatusCode());
        }
    }



}