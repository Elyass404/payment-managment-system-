package org.view;

import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;

import java.util.List;

import java.util.Scanner;

public class DirectorMenu {

    private final Agent currentAgent;
    private final AgentService agentService;
    private final PaymentService paymentService;
    private final DepartmentService departmentService;
    private final Scanner scanner;

    public DirectorMenu(Agent currentAgent, AgentService agentService,
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
            System.out.println("\n==== DIRECTOR MENU ====");
            System.out.println("1. View Personal Info");
            System.out.println("2. My Payments History");
            System.out.println("3. Calculate My Total Payments");
            System.out.println("4. Manage Departments");
            System.out.println("5. Manage Agents");
            System.out.println("6. Manage Payments");
            System.out.println("7. View Statistics");
            System.out.println("8. Logout");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewPersonalInfo();
                case "2" -> viewMyPayments();
                case "3" -> calculateMyTotalPayments();
                case "4" -> manageDepartments();
                case "5" -> manageAgents();
                case "6" -> managePayments();
                case "7" -> viewStatistics();
                case "8" -> exit = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ======== Personal Info ========
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

    // ======== Departments ========
    private void manageDepartments() {
        System.out.println("\n--- Departments ---");
        System.out.println("1. Add Department");
        System.out.println("2. Update Department");
        System.out.println("3. Delete Department");
        System.out.println("4. View All Departments");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addDepartment();
            case "2" -> updateDepartment();
            case "3" -> deleteDepartment();
            case "4" -> viewAllDepartments();
            case "5" -> {}
            default -> System.out.println("Invalid option.");
        }
    }

    private void addDepartment() {
        try {
            System.out.print("Department Name: ");
            String name = scanner.nextLine();
            Department dep = new Department();
            dep.setName(name);
            departmentService.addDepartment(currentAgent, dep);
            System.out.println("Department added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateDepartment() {
        try {
            System.out.print("Department ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            Department dep = departmentService.getDepartmentById(id);
            if (dep == null) {
                System.out.println("Department not found!");
                return;
            }
            System.out.print("New Name (" + dep.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) dep.setName(name);

            departmentService.updateDepartment(currentAgent, dep);
            System.out.println("Department updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteDepartment() {
        try {
            System.out.print("Department ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            departmentService.deleteDepartment(currentAgent, id);
            System.out.println("Department deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        for (Department d : departments) {
            System.out.println(d.getIdDepartment() + " | " + d.getName());
        }
    }

    // ======== Agents ========
    private void manageAgents() {
        System.out.println("\n--- Agents ---");
        System.out.println("1. Add Agent");
        System.out.println("2. Update Agent");
        System.out.println("3. Delete Agent");
        System.out.println("4. View All Agents");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addAgent();
            case "2" -> updateAgent();
            case "3" -> deleteAgent();
            case "4" -> viewAllAgents();
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
            System.out.print("Type (Worker/Responsible/Director/Intern): ");
            String type = scanner.nextLine();
            System.out.print("Department ID: ");
            int depId = Integer.parseInt(scanner.nextLine());
            Department dep = departmentService.getDepartmentById(depId);
            if (dep == null) {
                System.out.println("Department not found!");
                return;
            }

            Agent agent = new Agent();
            agent.setFirstName(firstName);
            agent.setLastName(lastName);
            agent.setEmail(email);
            agent.setPassword(password);
            agent.setTypeAgent(type);
            agent.setDepartment(dep);

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
            if (agent == null) {
                System.out.println("Agent not found!");
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

            System.out.print("New Department ID (" + agent.getDepartment().getIdDepartment() + "): ");
            String depInput = scanner.nextLine();
            if (!depInput.isBlank()) {
                Department dep = departmentService.getDepartmentById(Integer.parseInt(depInput));
                if (dep != null) agent.setDepartment(dep);
            }

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
            boolean success = agentService.deleteAgent(currentAgent, id);
            System.out.println(success ? "Agent deleted successfully!" : "Delete failed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllAgents() {
        List<Agent> agents = agentService.getAllAgents();
        for (Agent a : agents) {
            System.out.println(a.getIdAgent() + " | " + a.getFirstName() + " " + a.getLastName() + " | " +
                    a.getTypeAgent() + " | Department: " + (a.getDepartment() != null ? a.getDepartment().getName() : "None"));
        }
    }

    // ======== Payments ========
    private void managePayments() {
        System.out.println("\n--- Payments ---");
        System.out.println("1. Add Payment");
        System.out.println("2. Update Payment");
        System.out.println("3. Delete Payment");
        System.out.println("4. View Payments by Agent");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addPayment();
            case "2" -> updatePayment();
            case "3" -> deletePayment();
            case "4" -> viewPaymentsByAgent();
            case "5" -> {}
            default -> System.out.println("Invalid option.");
        }
    }

    private void addPayment() {
        try {
            System.out.print("Agent ID: ");
            int agentId = Integer.parseInt(scanner.nextLine());
            System.out.print("Payment Type (Salary/Bonus/Indemnity/etc): ");
            String type = scanner.nextLine();
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            Agent agent = agentService.getAgentById(agentId);
            if (agent == null) {
                System.out.println("Agent not found!");
                return;
            }

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

    private void updatePayment() {
        try {
            System.out.print("Payment ID to update: ");
            int paymentId = Integer.parseInt(scanner.nextLine());
            Payment payment = paymentService.getAllPayments().stream()
                    .filter(p -> p.getPaymentId() == paymentId)
                    .findFirst()
                    .orElse(null);

            if (payment == null) {
                System.out.println("Payment not found!");
                return;
            }

            System.out.print("New Payment Type (" + payment.getPaymentType() + "): ");
            String type = scanner.nextLine();
            if (!type.isBlank()) payment.setPaymentType(type);

            System.out.print("New Amount (" + payment.getAmount() + "): ");
            String amountInput = scanner.nextLine();
            if (!amountInput.isBlank()) payment.setAmount(Double.parseDouble(amountInput));

            boolean success = paymentService.updatePayment(currentAgent, payment);
            System.out.println(success ? "Payment updated successfully!" : "Update failed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deletePayment() {
        try {
            System.out.print("Payment ID to delete: ");
            int paymentId = Integer.parseInt(scanner.nextLine());
            Payment payment = paymentService.getAllPayments().stream()
                    .filter(p -> p.getPaymentId() == paymentId)
                    .findFirst()
                    .orElse(null);

            if (payment == null) {
                System.out.println("Payment not found!");
                return;
            }

            boolean success = paymentService.deletePayment(currentAgent, payment);
            System.out.println(success ? "Payment deleted successfully!" : "Delete failed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewPaymentsByAgent() {
        try {
            System.out.print("Agent ID: ");
            int agentId = Integer.parseInt(scanner.nextLine());
            List<Payment> payments = paymentService.getPaymentsByAgent(agentId);

            if (payments.isEmpty()) {
                System.out.println("No payments found for this agent.");
            } else {
                for (Payment p : payments) {
                    System.out.println(p.getPaymentId() + " | " + p.getPaymentType() + " | " + p.getAmount());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======== Statistics ========
    private void viewStatistics() {
        System.out.println("\n--- Statistics ---");
        System.out.println("1. Total Payments by Department");
        System.out.println("2. Average Salary by Department");
        System.out.println("3. Rank Agents by Total Payments");
        System.out.println("4. Global Report");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> totalPaymentsByDepartment();
            case "2" -> averageSalaryByDepartment();
            case "3" -> rankAgentsByTotalPayments();
            case "4" -> globalReport();
            case "5" -> {}
            default -> System.out.println("Invalid option.");
        }
    }

    private void totalPaymentsByDepartment() {
        try {
            System.out.print("Department ID: ");
            int depId = Integer.parseInt(scanner.nextLine());
            List<Agent> agents = agentService.getAgentsByDepartment(currentAgent, depId);
            double total = paymentService.calculateTotalPaymentsByDepartment(currentAgent, depId, agents);
            System.out.println("Total payments for department: " + total);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void averageSalaryByDepartment() {
        try {
            System.out.print("Department ID: ");
            int depId = Integer.parseInt(scanner.nextLine());
            List<Agent> agents = agentService.getAgentsByDepartment(currentAgent, depId);
            double avg = paymentService.calculateAverageSalaryByDepartment(currentAgent, depId, agents);
            System.out.println("Average salary for department: " + avg);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void rankAgentsByTotalPayments() {
        try {
            System.out.print("Department ID: ");
            int depId = Integer.parseInt(scanner.nextLine());
            List<Agent> agents = agentService.getAgentsByDepartment(currentAgent, depId);
            List<Agent> ranked = paymentService.rankAgentsByTotalPayment(currentAgent, agents);
            System.out.println("Agents ranked by total payments:");
            for (Agent a : ranked) {
                double total = paymentService.calculateTotalPaymentsByAgent(a.getIdAgent());
                System.out.println(a.getIdAgent() + " | " + a.getFirstName() + " " + a.getLastName() + " | Total: " + total);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void globalReport() {
        try {
            System.out.println("--- Global Report ---");
            int totalAgents = agentService.getAllAgents().size();
            int totalDepartments = departmentService.getAllDepartments().size();
            System.out.println("Total Agents: " + totalAgents);
            System.out.println("Total Departments: " + totalDepartments);

            List<Payment> payments = paymentService.getAllPayments();
            long salaryCount = payments.stream().filter(p -> p.getPaymentType().equalsIgnoreCase("Salary")).count();
            long bonusCount = payments.stream().filter(p -> p.getPaymentType().equalsIgnoreCase("Bonus")).count();
            long indemnityCount = payments.stream().filter(p -> p.getPaymentType().equalsIgnoreCase("Indemnity")).count();
            long otherCount = payments.size() - (salaryCount + bonusCount + indemnityCount);

            System.out.println("Payment distribution:");
            System.out.println("Salary: " + salaryCount * 100.0 / payments.size() + "%");
            System.out.println("Bonus: " + bonusCount * 100.0 / payments.size() + "%");
            System.out.println("Indemnity: " + indemnityCount * 100.0 / payments.size() + "%");
            System.out.println("Other: " + otherCount * 100.0 / payments.size() + "%");

            Payment maxPayment = payments.stream().max((p1, p2) -> Double.compare(p1.getAmount(), p2.getAmount())).orElse(null);
            if (maxPayment != null) {
                System.out.println("Agent with highest payment: " + maxPayment.getAgent().getFirstName() + " " + maxPayment.getAgent().getLastName() +
                        " | Amount: " + maxPayment.getAmount());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
