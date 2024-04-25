package com.example.university.role.model.mapper;

import com.example.university.role.model.dto.RoleDto;
import com.example.university.role.model.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    public RoleDto toDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    public Role fromDto(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

    public Set<RoleDto> toDtos(Set<Role> roles) {
        return roles.stream().map(this::toDto).collect(Collectors.toSet());
    }

}
