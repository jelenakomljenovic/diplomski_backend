package com.example.university.major.service;

import com.example.university.department.model.Department;
import com.example.university.department.repository.DepartmentRepository;
import com.example.university.major.model.Major;
import com.example.university.major.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MajorService {

    private final MajorRepository majorRepository;

    public Major insert(Major major){
        return majorRepository.saveAndFlush(major);
    }

    public void deleteMajorByDepartmentId(Long departmentId) {
        majorRepository.deleteByDepartmentId(departmentId);
    }


}
