package org;
import org.dao.PaymentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.serviceImpl.AgentServiceImpl;
import org.service.serviceImpl.DepartmentServiceImpl;
import util.JdbcConnectionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Testing {
    public static void main(String[] args){

        try {
            var conn = JdbcConnectionManager.getInstance().getConnection();
            System.out.println("The connection between the project and the database is done successfully: " + conn);
            //conn.close();

            AgentService agentService = new AgentServiceImpl(conn);

            int departmentId = 15;

            System.out.println("=== All Agents in Department " + departmentId + " ===");
            List<Agent> agents = agentService.getAgentsByDepartment(departmentId);
            for (Agent a : agents) {
                System.out.println(a.getIdAgent() + " - " + a.getFirstName() + " " + a.getLastName()
                        + " | Responsible: " + a.getIsResponsible());
            }

            // 3. Test "Find the responsible agent of a department"
            System.out.println("\n=== Responsible Agent in Department " + departmentId + " ===");
            Agent responsible = agentService.getResponsibleOfDepartment(departmentId);
            if (responsible != null) {
                System.out.println("Responsible: " + responsible.getFirstName() + " " + responsible.getLastName());
            } else {
                System.out.println("No responsible agent found for this department.");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}