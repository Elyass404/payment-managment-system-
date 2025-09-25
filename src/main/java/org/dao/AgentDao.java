package org.dao;

import org.model.Agent;

import java.util.List;

public interface AgentDao {
    void addAgent(Agent agent);
    List<Agent> getAllAgents();
    void updateAgent(Agent agent);
    void deleteAgent(Agent agent);
    Agent getAgentById(int id);
    Agent getAgentByName(String name);
    List<Agent> getAgentsByName(String name);
}
