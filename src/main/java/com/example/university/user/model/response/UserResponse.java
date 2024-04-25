package com.example.university.user.model.response;

import com.example.university.role.model.dto.RoleDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String image;
    private Set<RoleDto> roles;
}
