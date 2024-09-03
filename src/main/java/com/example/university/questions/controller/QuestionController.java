package com.example.university.questions.controller;

import com.example.university.questions.model.Question;
import com.example.university.questions.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<Question> findAllQuestions() {
        return questionService.getQuestions();
    }

    @PutMapping("/update/{questionId}")
    public Question update(@RequestBody Question question, @PathVariable Long questionId) {
        return questionService.updateQuestion(questionId, question);
    }
}
