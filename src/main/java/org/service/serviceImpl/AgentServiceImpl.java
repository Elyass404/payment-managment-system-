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
    public void addAgent(Agent currentAgent, Agent agent) {
        agentDao.addAgent(agent);
    }

    @Override
    public List<Agent> getAllAgents(Agent currentAgent) {

        return agentDao.getAllAgents();
    }

    @Override
    public Agent getAgentById(int id) {
        return agentDao.getAgentById(id).orElse(null);
    }

    @Override
    public boolean updateAgent(Agent currentAgent, Agent agent) {
        try {
            agentDao.updateAgent(agent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAgent(Agent currentAgent, int id) {
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

    //------- Authentication Methods ---------------
    @Override
    public Agent login(String email, String password) {
        return getAllAgents(Agent currentAgent).stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email) && a.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isDirector(Agent agent) {
        return agent.getTypeAgent().equalsIgnoreCase("Director");
    }

    @Override
    public boolean isResponsible(Agent agent) {
        return agent.getTypeAgent().equalsIgnoreCase("Responsible");
    }

    //------- Other Methods related to the Agent --------

    @Override
    public void AssignResponsible(Agent currentAgent, int agentId, int departmentId) {
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

    @Override
    public List<Agent> getAgentsByDepartment(Agent currentAgent, int departmentId){
        return agentDao.getAgentsByDepartment(departmentId);
    }

    @Override
    public Agent getResponsibleOfDepartment(Agent currentAgent, int departmentId){
        return agentDao.getResponsibleOfDepartment(departmentId).orElse(null);
    }

    //new method

    public static void main(String[] args) throws SQLException {
        // 1. Create a database connection
        Connection connection = JdbcConnectionManager.getInstance().getConnection(); // your utility to get a JDBC connection
    }
}
