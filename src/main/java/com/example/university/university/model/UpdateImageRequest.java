package com.example.university.university.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateImageRequest {

    private String oldName;

    private String oldCity;

    private String newName;

    private String newCity;
}
