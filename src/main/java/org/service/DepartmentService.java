package org.service;

import org.model.Department;

import java.util.List;

public interface DepartmentService {
   void addDepartment(Department department);
   List<Department> getAllDepartments ();
   Department getDepartmentById(int id);
   void updateDepartment(Department department);
   void deleteDepartment(int id);

}
