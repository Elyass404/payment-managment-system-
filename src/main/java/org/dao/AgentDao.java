package org.dao;

import org.model.Agent;

import java.util.List;

public interface AgentDao {
    void addAgent(Agent agent);
    Agent getAgentById(int id);
    List<Agent> getAllAgents();
    void updateAgent(Agent agent);
    void deleteAgent(Agent agent);
}
