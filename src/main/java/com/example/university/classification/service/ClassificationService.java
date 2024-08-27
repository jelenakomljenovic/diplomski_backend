package com.example.university.classification.service;

import com.example.university.classification.model.Classification;
import com.example.university.classification.repository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassificationService {

    private final ClassificationRepository classificationRepository;

    public List<Classification> findAll() {
        return classificationRepository.findAll();
    }
}
