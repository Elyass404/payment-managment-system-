package org.service;

import org.model.Agent;
import org.model.Department;

import java.util.List;

public interface DepartmentService {

    //Crud methods
   void addDepartment(Agent currentAgent, Department department);
   List<Department> getAllDepartments ();
   Department getDepartmentById(int id);
   void updateDepartment(Agent currentAgent, Department department);
   void deleteDepartment(Agent currentAgent, int id);

}
