package org.service.serviceImpl;

import org.dao.DepartmentDao;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Department;
import org.service.DepartmentService;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao ;

    public DepartmentServiceImpl(Connection connection) {
        this.departmentDao = new DepartmentDaoImpl(connection);
    }

    @Override
    public void addDepartment(Department department) {
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
    public void updateDepartment(Department department) {
        departmentDao.updateDepartment(department);
    }

    @Override
    public void deleteDepartment(int id) {
        // better to fetch first (optional)
        Department department = getDepartmentById(id);
        if (department != null) {
            departmentDao.deleteDepartment(department);
        } else {
            System.out.println("Department with id " + id + " not found!");
        }
    }


}
