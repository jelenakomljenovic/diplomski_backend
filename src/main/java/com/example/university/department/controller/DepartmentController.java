package com.example.university.department.controller;

import com.example.university.department.model.Department;
import com.example.university.department.service.DepartmentService;
import com.example.university.university.model.University;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Department>> getAllDepartments(@PathVariable Long id){
        List<Department> departments = departmentService.findAllByUniversityId(id);
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/insert")
    public ResponseEntity<Department> insertDepartment(@RequestBody Department department) {
        Department department1 = departmentService.insert(department);
        return ResponseEntity.ok(department1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department department1 = departmentService.update(id, department);
        return ResponseEntity.ok(department);
    }
}
