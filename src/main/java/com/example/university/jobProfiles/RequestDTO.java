package com.example.university.jobProfiles;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDTO {
    private List<String> faculties;

    private int num_responsibilities;

    private int num_skills;

}
