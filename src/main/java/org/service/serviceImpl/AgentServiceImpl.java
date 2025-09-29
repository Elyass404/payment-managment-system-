package org.service.serviceImpl;

import org.dao.AgentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.model.Agent;
import org.service.AgentService;

import java.sql.Connection;
import java.util.List;

public class AgentServiceImpl implements AgentService {

    private final AgentDao agentDao;

    // Constructor injection for DAO
    public AgentServiceImpl(Connection connection) {
        this.agentDao = new AgentDaoImpl(connection);
    }

    @Override
    public void AssignResponsible(int agentId, int departmentId) {
        // TODO: we will implement this later when we define the "responsible" logic;
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
}
