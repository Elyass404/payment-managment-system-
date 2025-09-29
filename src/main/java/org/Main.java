package org;
import com.mysql.cj.jdbc.Driver;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Agent;
import org.model.Department;
import util.JdbcConnectionManager;

import java.util.Optional;


public class Main {
    public static void main(String[] args){

        try {
        var conn = JdbcConnectionManager.getInstance().getConnection();
        System.out.println("The connection between the project and the database is done successfully: " + conn);
        //conn.close();

        DepartmentDaoImpl departmentDao = new DepartmentDaoImpl(conn);
        Department dep = new Department("Dev");
        departmentDao.addDepartment(dep);

        Optional<Department> fetchedDep = departmentDao.getDepartmentById(dep.getIdDepartment());
        fetchedDep.ifPresent(d -> System.out.println("Department found: " + d.getName()));

        // Test AgentDao
        AgentDaoImpl agentDao = new AgentDaoImpl(conn);
        Agent agent = new Agent("ilyass", "test", "ilyass.test@example.com", "1234","Worker", dep);

        agentDao.addAgent(agent);

        Optional<Agent> getAgent = agentDao.getAgentById(agent.getIdAgent());
        getAgent.ifPresent(a -> System.out.println("Agent found: "+a.getIdAgent()+" | " + a.getFirstName() + " " + a.getLastName()));

    } catch (Exception e){
        e.printStackTrace();
    }
}
}