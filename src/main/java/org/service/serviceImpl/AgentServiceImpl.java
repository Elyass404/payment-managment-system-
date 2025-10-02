package org.service.serviceImpl;

import org.dao.AgentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.service.AgentService;
import util.JdbcConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AgentServiceImpl implements AgentService {

    private final AgentDao agentDao;

    // Constructor injection for DAO
    public AgentServiceImpl(Connection connection) {
        this.agentDao = new AgentDaoImpl(connection);
    }


    @Override
    public void addAgent(Agent agent) {
        agentDao.addAgent(agent);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentDao.getAllAgents();
    }

    @Override
    public Agent getAgentById(int id) {
        // DAO returns Optional<Agent>, here we decide how to handle missing agent
        return agentDao.getAgentById(id).orElse(null);
    }

    @Override
    public boolean updateAgent(Agent agent) {
        try {
            agentDao.updateAgent(agent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAgent(int id) {
        try {
            // Instead of passing an Agent object, we only need the ID
            Agent agent = getAgentById(id);
            if (agent != null) {
                agentDao.deleteAgent(agent);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //------- Other Methods related to the Agent --------

    @Override
    public void AssignResponsible(int agentId, int departmentId) {
        // Get all agents in the department
        List<Agent> agentsInDept = agentDao.getAllAgents().stream()
                .filter(a -> a.getDepartment() != null && a.getDepartment().getIdDepartment() == departmentId)
                .toList();
        // Unset current responsible if exists
        for (Agent agent : agentsInDept) {
            if (agent.getIsResponsible()) {
                agent.setIsResponsible(false);
                agentDao.updateAgent(agent);
            }
        }

        //Get the new responsible agent
        Agent newResponsible = agentDao.getAgentById(agentId).orElse(null);

        if (newResponsible != null) {
            //Validate agent belongs to the department
            if (newResponsible.getDepartment() != null &&
                    newResponsible.getDepartment().getIdDepartment() == departmentId) {

                //Assign as responsible
                newResponsible.setIsResponsible(true);
                agentDao.updateAgent(newResponsible);
                System.out.println("Agent " + newResponsible.getFirstName() + " is now the responsible of department " + departmentId);
            } else {
                System.out.println("Error: Agent does not belong to the specified department.");
            }
        } else {
            System.out.println("Error: Agent not found.");
        }
    }

    //new method

    public static void main(String[] args) throws SQLException {
        // 1. Create a database connection
        Connection connection = JdbcConnectionManager.getInstance().getConnection(); // your utility to get a JDBC connection

        // 2. Initialize the service
        AgentServiceImpl agentService = new AgentServiceImpl(connection);
        DepartmentDaoImpl departmentDao = new DepartmentDaoImpl(connection);

        Optional<Department> dep = departmentDao.getDepartmentById(15);

        agentService.addAgent(new Agent("Hmad", "Si Brahim", "sibrahim.hmad@example.com", "1234", "Worker", dep.get()));

        // 3. Retrieve all agents (optional, just to see initial state)
        System.out.println("Agents before assigning responsible:");
        agentService.getAllAgents().forEach(a ->
                System.out.println(a.getIdAgent() + " - " + a.getFirstName() + " " + a.getLastName() + " | Responsible: " + a.getIsResponsible())
        );

        // 4. Assign a responsible agent to a department
        int agentIdToAssign = 24;       // replace with a valid agent ID
        int departmentId = 15;          // replace with a valid department ID
        agentService.AssignResponsible(agentIdToAssign, departmentId);

        // 5. Retrieve all agents again to verify change
        System.out.println("\nAgents after assigning responsible:");
        agentService.getAllAgents().forEach(a ->
                System.out.println(a.getIdAgent() + " - " + a.getFirstName() + " " + a.getLastName() + " | Responsible: " + a.getIsResponsible())
        );

        // 6. Close the connection
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
