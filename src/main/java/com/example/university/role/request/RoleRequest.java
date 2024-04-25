package com.example.university.role.request;

import com.example.university.role.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleRequest {

    private Long userId;

    private List<RoleEnum> roleEnum;
}
