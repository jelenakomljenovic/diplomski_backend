package com.example.university.jobProfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-profile")
public class JobProfileController {

    @Autowired
    private JobProfileService jobProfileService;

    @PostMapping("/random-responsibilities")
    public Map<String, List<String>> getRandomResponsibilities(@RequestBody RequestDTO request) {
        List<String> faculties = request.getFaculties();
        int numResponsibilities = request.getNum_responsibilities();
        return jobProfileService.getRandomResponsibilities(faculties, numResponsibilities);
    }

    @PostMapping("/random-skills")
    public Map<String, List<String>> getRandomSkills(@RequestBody RequestDTO request) {
        List<String> faculties = request.getFaculties();
        int skillsResponsibilities = request.getNum_skills();
        return jobProfileService.getRandomSkills(faculties, skillsResponsibilities);
    }

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody String textRequest) {
        if (textRequest == null) {
            return ResponseEntity.badRequest().body("No text provided");
        }
        try {
            String prediction = jobProfileService.preprocessAndPredict(textRequest);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}