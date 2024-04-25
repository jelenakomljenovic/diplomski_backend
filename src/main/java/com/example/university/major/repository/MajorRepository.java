package com.example.university.major.repository;

import com.example.university.department.model.Department;
import com.example.university.major.model.Major;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {

    @Transactional
    void deleteByDepartmentId(Long departmentId);

}
