package com.example.university.department.service;

import com.example.university.department.model.Department;
import com.example.university.department.repository.DepartmentRepository;
import com.example.university.exception.ResourceNotFoundException;
import com.example.university.exception.UniversityNotFoundException;
import com.example.university.major.model.Major;
import com.example.university.major.repository.MajorRepository;
import com.example.university.university.Location;
import com.example.university.university.OSMService;
import com.example.university.university.model.University;
import com.example.university.university.repository.UniversityRepository;
import com.example.university.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final MajorRepository majorRepository;

    public List<Department> findAllByUniversityId(Long universityId) {
        List<Department> allDepartments = departmentRepository.findAll();

        List<Department> filteredDepartments = allDepartments.stream()
                .filter(department -> universityId == department.getUniversity().getId())
                .collect(Collectors.toList());

        return filteredDepartments;
    }
    public Department insert(Department department){
        Department department1 = departmentRepository.saveAndFlush(department);
        for(Major major: department.getMajors()){
            Major major1 = new Major();
            major1.setDepartment(department1);
            major1.setName(major.getName());
            majorRepository.saveAndFlush(major1);
        }
        return department1;
    }

    public Department update(Long id, Department department){
        Department department1 = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department with id: " + department.getId() + " not found!"));;
        department1.setName(department.getName());
        Department department2 = departmentRepository.saveAndFlush(department1);
        for(Major major: department1.getMajors()){
            majorRepository.deleteById(major.getId());
        }
        for(Major major_2: department.getMajors()){
            Major major2 = new Major();
            major2.setDepartment(department2);
            major2.setName(major_2.getName());
            majorRepository.saveAndFlush(major2);
        }
        return department;
    }

    public void deleteDepartment(Long id){
        majorRepository.deleteByDepartmentId(id);
        departmentRepository.deleteById(id);

    }


}
