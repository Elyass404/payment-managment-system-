package org.service;

import org.model.Agent;

import java.util.List;

public interface AgentService {
        //Crud methods
        void addAgent(Agent currentAgent, Agent agent);
        List<Agent> getAllAgents();
        Agent getAgentById(int id);
        boolean updateAgent(Agent currentAgent, Agent agent);
        boolean deleteAgent(Agent currentAgent, int id);

        //authentication methods
        Agent login(String email, String password);
        boolean isDirector(Agent agent);
        boolean isResponsible(Agent agent);


    //other methods to add
        void AssignResponsible(Agent currentAgent,int agentId, int DepartmentId);
        List<Agent> getAgentsByDepartment(Agent currentAgent, int departmentId);
        Agent getResponsibleOfDepartment(Agent currentAgent, int departmentId);
    }

