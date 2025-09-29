package org;
import org.model.Agent;
import org.model.Department;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.serviceImpl.AgentServiceImpl;
import org.service.serviceImpl.DepartmentServiceImpl;
import util.JdbcConnectionManager;

import java.util.List;


public class Main {
    public static void main(String[] args){

        try {
        var conn = JdbcConnectionManager.getInstance().getConnection();
        System.out.println("The connection between the project and the database is done successfully: " + conn);
        //conn.close();

            // ----- Department Service -----
            DepartmentService departmentService = new DepartmentServiceImpl(conn);

            Department dep = new Department("HR");
            departmentService.addDepartment(dep);

            Department fetchedDep = departmentService.getDepartmentById(dep.getIdDepartment());
            System.out.println("📌 Department found: " + fetchedDep.getName());

            // ----- Agent Service -----
            AgentService agentService = new AgentServiceImpl(conn);

            Agent agent = new Agent("Alien", "hello", "alien.hello@example.com", "1234", "Worker", dep);
            agentService.addAgent(agent);

            // Update the agent
            agent.setEmail("alien.helloaw@example.com");
            agentService.updateAgent(agent);

            // Fetch agent by ID
            Agent fetchedAgent = agentService.getAgentById(agent.getIdAgent());
            if (fetchedAgent != null) {
                System.out.println("Agent found: " +
                        fetchedAgent.getIdAgent() + " | " +
                        fetchedAgent.getFirstName() + " " +
                        fetchedAgent.getLastName() + " | " +
                        fetchedAgent.getEmail());
            }

            // Get all agents
            List<Agent> allAgents = agentService.getAllAgents();
            System.out.println("List of Agents:");
            for (Agent a : allAgents) {
                System.out.println(" - " + a.getIdAgent() + " | " + a.getFirstName() + " " + a.getLastName());
            }

            // Test delete
            // agentService.deleteAgent(agent.getIdAgent());

        } catch (Exception e){
        e.printStackTrace();
    }
}
}