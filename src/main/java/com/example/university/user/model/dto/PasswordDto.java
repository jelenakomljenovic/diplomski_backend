package com.example.university.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordDto {

    private String password;

    private String token;

}
