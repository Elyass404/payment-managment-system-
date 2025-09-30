package org.service;

import org.model.Agent;

import java.util.List;

public interface AgentService {
        //Crud methods
        void addAgent(Agent agent);
        List<Agent> getAllAgents();
        Agent getAgentById(int id);
        boolean updateAgent(Agent agent);
        boolean deleteAgent(int id);

        //other methods to add
        void AssignResponsible(int agentId, int DepartmentId);
    }

