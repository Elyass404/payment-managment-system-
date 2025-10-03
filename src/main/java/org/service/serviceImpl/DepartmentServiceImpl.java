package org.service.serviceImpl;

import org.dao.DepartmentDao;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Department;
import org.model.Agent;
import org.service.AgentService;
import org.service.DepartmentService;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao ;
    private final AgentService agentService ;

    public DepartmentServiceImpl(Connection connection, AgentService agentService) {
        this.departmentDao = new DepartmentDaoImpl(connection);
        this.agentService = agentService;
    }

    @Override
    public void addDepartment(Agent currentAgent, Department department) {
        if (!agentService.isDirector(currentAgent)) {
            throw new SecurityException("Only the Director can add departments.");
        }
        departmentDao.addDepartment(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }

    @Override
    public Department getDepartmentById(int id) {
        Optional<Department> dep = departmentDao.getDepartmentById(id);
        return dep.orElse(null); // you could throw exception instead if not found
    }

    @Override
    public void updateDepartment(Agent currentAgent, Department department) {
        if (!agentService.isDirector(currentAgent)) {
            throw new SecurityException("Only the Director can update departments.");
        }
        departmentDao.updateDepartment(department);
    }


    @Override
    public void deleteDepartment(Agent currentAgent, int id) {
        if (!agentService.isDirector(currentAgent)) {
            throw new SecurityException("Only the Director can delete departments.");
        }

        Department department = getDepartmentById(id);
        if (department != null) {
            departmentDao.deleteDepartment(department);
        } else {
            System.out.println("Department with id " + id + " not found!");
        }
    }



}
