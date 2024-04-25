package com.example.university.role.repository;

import com.example.university.role.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, RevisionRepository<Role, Long, Long> {

    Optional<Role> findByName(String name);
}
