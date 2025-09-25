package org.dao;

import org.model.Department;

import java.util.List;

public interface DepartmentDao {
    void addDepartment(Department department);
    Department getDepartmentById(int id );
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(Department id);
}
