package org;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;
import org.service.serviceImpl.AgentServiceImpl;
import org.service.serviceImpl.DepartmentServiceImpl;
import org.service.serviceImpl.PaymentServiceImpl;
import util.JdbcConnectionManager;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args){

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            System.out.println("Database connected: " + conn);

            // --- Prepare Department and Agent ---
            DepartmentDaoImpl departmentDao = new DepartmentDaoImpl(conn);
            Department dep = new Department("techy");
            departmentDao.addDepartment(dep);

            Optional<Department> fetchedDep = departmentDao.getDepartmentById(dep.getIdDepartment());
            fetchedDep.ifPresent(d -> System.out.println("Department fetched: " + d.getName()));

            AgentDaoImpl agentDao = new AgentDaoImpl(conn);
            Agent agent = new Agent("Hmad", "Si Brahim", "sibrahim.hmad@example.com", "1234", "Worker", dep);
            agentDao.addAgent(agent);

            Optional<Agent> fetchedAgent = agentDao.getAgentById(agent.getIdAgent());
            fetchedAgent.ifPresent(a -> System.out.println("Agent fetched: " + a.getFirstName()));

            // --- Payment Service ---
            PaymentService paymentService = new PaymentServiceImpl(conn);

            // Create payments
            Payment p1 = new Payment(agent, 1500.0, "Salary", true, LocalDateTime.now(), "January Salary");
            Payment p2 = new Payment(agent, 300.0, "Bonus", true, LocalDateTime.now(), "Project Bonus");

            // Add payments
            paymentService.addPayment(p1);
            paymentService.addPayment(p2);

            // Fetch and display all payments for the agent
            List<Payment> payments = paymentService.getPaymentsByAgent(agent.getIdAgent());
            System.out.println("\nPayments for Agent " + agent.getFirstName() + ":");
            for (Payment p : payments) {
                System.out.println(p.getPaymentType() + " | " + p.getAmount() + " | " + p.getDate() + " | " + p.getReason());
            }

            // Calculate total payments
            double total = paymentService.calculateTotalPaymentsByAgent(agent.getIdAgent());
            System.out.println("\nTotal Payments for " + agent.getFirstName() + ": " + total);

            // Filter by type
            List<Payment> salaryPayments = paymentService.filterPaymentsByType(agent.getIdAgent(), "Salary");
            System.out.println("\nSalary Payments:");
            salaryPayments.forEach(p -> System.out.println(p.getAmount() + " | " + p.getReason()));

            // Sort payments by date
            List<Payment> sortedPayments = paymentService.sortPaymentsByDate(agent.getIdAgent());
            System.out.println("\nPayments sorted by date:");
            sortedPayments.forEach(p -> System.out.println(p.getDate() + " | " + p.getPaymentType()));

        } catch (Exception e) {
            e.printStackTrace();
        }
}
}