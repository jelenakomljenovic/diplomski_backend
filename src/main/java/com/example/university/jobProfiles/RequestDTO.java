package com.example.university.jobProfiles;

import java.util.List;

public class RequestDTO {
    private List<String> faculties;
    private int num_responsibilities;
    private int num_skills;

    public List<String> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<String> faculties) {
        this.faculties = faculties;
    }

    public int getNum_responsibilities() {
        return num_responsibilities;
    }

    public void setNum_responsibilities(int num_responsibilities) {
        this.num_responsibilities = num_responsibilities;
    }

    public int getNum_skills() {
        return num_skills;
    }

    public void setNum_skills(int num_skills) {
        this.num_skills = num_skills;
    }
}
