package org.dao;

import org.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDao {
    void addDepartment(Department department);
    Optional<Department> getDepartmentById(int id );
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(Department id);
}
