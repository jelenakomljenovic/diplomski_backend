package com.example.university.classification.controller;

import com.example.university.classification.model.Classification;
import com.example.university.classification.service.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classifications")
public class ClassificationController {

    private final ClassificationService classificationService;

    @GetMapping
    public ResponseEntity<List<Classification>> getAllProfessions() {
        List<Classification> professions = classificationService.findAll();
        return ResponseEntity.ok(professions);
    }
}
