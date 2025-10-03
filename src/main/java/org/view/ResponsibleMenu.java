package org.view;

import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;

import java.util.List;
import java.util.Scanner;

public class ResponsibleMenu {

    private final Agent currentAgent;
    private final AgentService agentService;
    private final PaymentService paymentService;
    private final DepartmentService departmentService;
    private final Scanner scanner;

    public ResponsibleMenu(Agent currentAgent, AgentService agentService,
                           PaymentService paymentService, DepartmentService departmentService) {
        this.currentAgent = currentAgent;
        this.agentService = agentService;
        this.paymentService = paymentService;
        this.departmentService = departmentService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== RESPONSIBLE MENU ====");
            System.out.println("1. View Personal Info");
            System.out.println("2. My Payments History");
            System.out.println("3. Manage Agents in Department");
            System.out.println("4. Add Payment to Agent");
            System.out.println("5. Calculate Total My Payments");
            System.out.println("6. Logout");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewPersonalInfo();
                case "2" -> viewMyPayments();
                case "3" -> manageAgents();
                case "4" -> addPaymentToAgent();
                case "5" -> calculateMyTotalPayments();
                case "6" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ===== Worker actions =====
    private void viewPersonalInfo() {
        System.out.println("ID: " + currentAgent.getIdAgent());
        System.out.println("Name: " + currentAgent.getFirstName() + " " + currentAgent.getLastName());
        System.out.println("Email: " + currentAgent.getEmail());
        System.out.println("Department: " + (currentAgent.getDepartment() != null ? currentAgent.getDepartment().getName() : "None"));
        System.out.println("Role: " + currentAgent.getTypeAgent());
    }

    private void viewMyPayments() {
        List<Payment> payments = paymentService.getPaymentsByAgent(currentAgent.getIdAgent());
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        } else {
            for (Payment p : payments) {
                System.out.println(p.getPaymentId() + " | " + p.getPaymentType() + " | " + p.getAmount());
            }
        }
    }

    private void calculateMyTotalPayments() {
        double total = paymentService.calculateTotalPaymentsByAgent(currentAgent.getIdAgent());
        System.out.println("Total payments: " + total);
    }

    // ===== Responsible-specific actions =====
    private void manageAgents() {
        System.out.println("\n--- Agents in Your Department ---");
        System.out.println("1. Add Agent");
        System.out.println("2. Update Agent");
        System.out.println("3. Delete Agent");
        System.out.println("4. View All Agents in Department");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addAgent();
            case "2" -> updateAgent();
            case "3" -> deleteAgent();
            case "4" -> viewDepartmentAgents();
            case "5" -> {}
            default -> System.out.println("Invalid option.");
        }
    }

    private void addAgent() {
        try {
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Type (Worker/Responsible/Intern): ");
            String type = scanner.nextLine();

            Agent agent = new Agent();
            agent.setFirstName(firstName);
            agent.setLastName(lastName);
            agent.setEmail(email);
            agent.setPassword(password);
            agent.setTypeAgent(type);
            agent.setDepartment(currentAgent.getDepartment());

            agentService.addAgent(currentAgent, agent);
            System.out.println("Agent added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateAgent() {
        try {
            System.out.print("Enter Agent ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            Agent agent = agentService.getAgentById(id);

            if (agent == null || !agent.getDepartment().equals(currentAgent.getDepartment())) {
                System.out.println("Agent not found in your department!");
                return;
            }

            System.out.print("New First Name (" + agent.getFirstName() + "): ");
            String firstName = scanner.nextLine();
            if (!firstName.isBlank()) agent.setFirstName(firstName);

            System.out.print("New Last Name (" + agent.getLastName() + "): ");
            String lastName = scanner.nextLine();
            if (!lastName.isBlank()) agent.setLastName(lastName);

            System.out.print("New Password: ");
            String password = scanner.nextLine();
            if (!password.isBlank()) agent.setPassword(password);

            boolean success = agentService.updateAgent(currentAgent, agent);
            System.out.println(success ? "Agent updated successfully!" : "Update failed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteAgent() {
        try {
            System.out.print("Enter Agent ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            Agent agent = agentService.getAgentById(id);

            if (agent == null || !agent.getDepartment().equals(currentAgent.getDepartment())) {
                System.out.println("Agent not found in your department!");
                return;
            }

            boolean success = agentService.deleteAgent(currentAgent, id);
            System.out.println(success ? "Agent deleted successfully!" : "Delete failed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewDepartmentAgents() {
        List<Agent> agents = agentService.getAgentsByDepartment(currentAgent, currentAgent.getDepartment().getIdDepartment());
        for (Agent a : agents) {
            System.out.println(a.getIdAgent() + " | " + a.getFirstName() + " " + a.getLastName() + " | " + a.getTypeAgent());
        }
    }

    private void addPaymentToAgent() {
        try {
            System.out.print("Agent ID: ");
            int agentId = Integer.parseInt(scanner.nextLine());
            Agent agent = agentService.getAgentById(agentId);

            if (agent == null || !agent.getDepartment().equals(currentAgent.getDepartment())) {
                System.out.println("Agent not found in your department!");
                return;
            }

            System.out.print("Payment Type (Salary/Bonus/Indemnity/etc): ");
            String type = scanner.nextLine();


            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            Payment payment = new Payment();
            payment.setAgent(agent);
            payment.setPaymentType(type);
            payment.setAmount(amount);

            String result = paymentService.addPayment(currentAgent, payment);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
