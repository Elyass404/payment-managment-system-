package org.dao.daoImpliment;

import java.sql.*;
import org.dao.DepartmentDao;
import org.model.Department;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {

    private Connection connection;

    public DepartmentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addDepartment(Department department) {
        String sql = "INSERT INTO department(name) VALUES (?)";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, department.getName());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("You successfully added a new department.");
            } else {
                System.out.println("Sorry, something went wrong when adding the department, please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Department> getDepartmentById(int id) {
        String sql = "SELECT * FROM department WHERE id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, id);

            try (ResultSet result = stmnt.executeQuery()) {
                if (result.next()) {
                    Department department = new Department();
                    department.setIdDepartment(result.getInt("id"));
                    department.setName(result.getString("name"));

                    return Optional.of(department);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    @Override
    public List<Department> getAllDepartments() {
        String sql = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();

        try (PreparedStatement stmnt = connection.prepareStatement(sql);
             ResultSet result = stmnt.executeQuery()) {

            while (result.next()) {
                Department department = new Department();
                department.setIdDepartment(result.getInt("id"));
                department.setName(result.getString("name"));

                departments.add(department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }


    @Override
    public void updateDepartment(Department department) {
        String sql = "UPDATE department SET name = ? WHERE id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, department.getName());
            stmnt.setInt(2, department.getIdDepartment());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Department updated successfully!");
            } else {
                System.out.println("Update failed: no department found with the given id.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteDepartment(Department department) {
        String sql = "DELETE FROM department WHERE id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, department.getIdDepartment());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Department deleted successfully!");
            } else {
                System.out.println("Delete failed: no department found with the given id.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
