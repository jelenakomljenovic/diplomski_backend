package com.example.university.questions.service;

import com.example.university.questions.model.Question;
import com.example.university.questions.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getQuestions() {
        List<Question> questions = questionRepository.findAll();

        return questions;
    }

    public Question updateQuestion(Long id, Question question) {
        Question question1 = questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        question1.setQuestion(question.getQuestion());
        question1 = questionRepository.saveAndFlush(question1);

        return question1;

    }


}
