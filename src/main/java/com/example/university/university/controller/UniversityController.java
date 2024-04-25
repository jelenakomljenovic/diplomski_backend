package com.example.university.university.controller;

import com.example.university.university.OSMService;
import com.example.university.university.model.University;
import com.example.university.university.model.UniversityKeyWordsRequest;
import com.example.university.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public ResponseEntity<List<University>> getAllFaculties() {
        List<University> facultiesList = universityService.findAll();
        return ResponseEntity.ok(facultiesList);
    }

    @GetMapping("/countries")
    public ResponseEntity<Set<String>> getAllCountries() {
        Set<String> countriesList = universityService.getAllCountries();
        return ResponseEntity.ok(countriesList);
    }

    @PostMapping("/cities")
    public ResponseEntity<Set<String>> getAllCities(@RequestBody Set<String> countries) {
        Set<String> citiesList = universityService.getAllCities(countries);
        return ResponseEntity.ok(citiesList);
    }

    private final OSMService osmService;


    @PostMapping("/insert")
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University university1 = universityService.insertUniversity(university);
        return ResponseEntity.ok(university1);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @RequestBody University university) {
        University university1 = universityService.updateUniversity(id, university);
        return ResponseEntity.ok(university1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> findUniversityById(@PathVariable Long id) {
        University university1 = universityService.findById(id);
        return ResponseEntity.ok(university1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/insert/keywords")
    public ResponseEntity<Void> insertKeyWords(@RequestBody UniversityKeyWordsRequest universityKeyWordsRequest) {
        universityService.updateUniversityKeywords(universityKeyWordsRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/filter")
    public ResponseEntity<List<University>> getAllFacultiesByKeyWords(@RequestBody List<String> keywords) {
        List<University> facultiesList = universityService.filterByKeyWords(keywords);
        return ResponseEntity.ok(facultiesList);
    }


}
