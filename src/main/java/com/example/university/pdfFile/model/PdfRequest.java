package com.example.university.pdfFile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfRequest {

    private String to;
    private String subject;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String abilities;
    private String skills;
    private String predictionResult;
}
