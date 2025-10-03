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

        //authentication methods
        Agent login(String email, String password);
        boolean isDirector(Agent agent);
        boolean isResponsible(Agent agent);


    //other methods to add
        void AssignResponsible(int agentId, int DepartmentId);
        List<Agent> getAgentsByDepartment(int departmentId);
        Agent getResponsibleOfDepartment(int departmentId);
    }

