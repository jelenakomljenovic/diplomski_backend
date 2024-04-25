package com.example.university.user.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
