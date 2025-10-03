package org.controller;

import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;
import util.PaymentValidator;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AppController {



    private final AgentService agentService;
    private final DepartmentService departmentService;
    private final PaymentService paymentService;

    private final Scanner scanner = new Scanner(System.in);

    public AppController(Connection connection, AgentService agentService,
                         DepartmentService departmentService, PaymentService paymentService) {
        this.agentService = agentService;
        this.departmentService = departmentService;
        this.paymentService = paymentService;
    }

    public void startApp() {
        System.out.println("Welcome to the Payment Management System");
        Agent loggedAgent = login();

        if (loggedAgent != null) {
            switch (loggedAgent.getTypeAgent()) {
                case "OUVRIER", "STAGIAIRE" -> agentMenu(loggedAgent);
                case "RESPONSABLE_DEPARTEMENT" -> responsibleMenu(loggedAgent);
                case "DIRECTEUR" -> directorMenu(loggedAgent);
                default -> System.out.println("Unknown role.");
            }
        }
    }

    // ---------------- Authentication ----------------
    private Agent login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Agent agent = agentService.getAllAgents()
                .stream()
                .filter(a -> a.getEmail().equals(email) && a.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (agent == null) {
            System.out.println("Invalid credentials.");
        } else {
            System.out.println("Logged in as: " + agent.getFirstName() + " " + agent.getLastName() +
                    " (" + agent.getTypeAgent() + ")");
        }

        return agent;
    }

    // ---------------- Agent Menu ----------------
    private void agentMenu(Agent agent) {
        int choice;
        do {
            System.out.println("\n--- Agent Menu ---");
            System.out.println("1. View my info");
            System.out.println("2. View my payments");
            System.out.println("3. Filter payments by type");
            System.out.println("4. Sort payments by date");
            System.out.println("5. Calculate total payments");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> displayAgentInfo(agent);
                case 2 -> displayPayments(agent);
                case 3 -> filterPaymentsByType(agent);
                case 4 -> sortPaymentsByDate(agent);
                case 5 -> calculateTotalPayments(agent);
            }
        } while (choice != 0);
    }

    // ---------------- Responsible Menu ----------------
    private void responsibleMenu(Agent agent) {
        int choice;
        do {
            System.out.println("\n--- Responsible Menu ---");
            System.out.println("1. Manage agents");
            System.out.println("2. Add payment to agent");
            System.out.println("3. View agent payments");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> manageAgents(agent);
                case 2 -> addPayment(agent);
                case 3 -> viewPaymentsOfAgent();
            }
        } while (choice != 0);
    }

    // ---------------- Director Menu ----------------
    private void directorMenu(Agent agent) {
        int choice;
        do {
            System.out.println("\n--- Director Menu ---");
            System.out.println("1. Manage departments");
            System.out.println("2. Manage agents");
            System.out.println("3. Add payment to agent");
            System.out.println("4. View agent payments");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> manageDepartments();
                case 2 -> manageAgents(agent);
                case 3 -> addPayment(agent);
                case 4 -> viewPaymentsOfAgent();
            }
        } while (choice != 0);
    }

    // ---------------- Agent Actions ----------------
    private void displayAgentInfo(Agent agent) {
        System.out.println("ID: " + agent.getIdAgent());
        System.out.println("Name: " + agent.getFirstName() + " " + agent.getLastName());
        System.out.println("Email: " + agent.getEmail());
        System.out.println("Department: " + (agent.getDepartment() != null ? agent.getDepartment().getName() : "None"));
        System.out.println("Role: " + agent.getTypeAgent());
    }

    private void displayPayments(Agent agent) {
        List<Payment> payments = paymentService.getPaymentsByAgent(agent.getIdAgent());
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        payments.forEach(this::printPayment);
    }

    private void filterPaymentsByType(Agent agent) {
        System.out.print("Enter payment type (Salary, Prime, Bonus, Indemnite): ");
        String type = scanner.nextLine();
        List<Payment> payments = paymentService.filterPaymentsByType(agent.getIdAgent(), type);
        if (payments.isEmpty()) {
            System.out.println("No payments found for this type.");
            return;
        }
        payments.forEach(this::printPayment);
    }

    private void sortPaymentsByDate(Agent agent) {
        List<Payment> payments = paymentService.sortPaymentsByDate(agent.getIdAgent());
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        payments.forEach(this::printPayment);
    }

    private void calculateTotalPayments(Agent agent) {
        double total = paymentService.calculateTotalPaymentsByAgent(agent.getIdAgent());
        System.out.println("Total payments: " + total);
    }

    // ---------------- Responsible/Director Actions ----------------
    private void manageAgents(Agent loggedAgent) {
        int choice;
        do {
            System.out.println("\n--- Manage Agents ---");
            System.out.println("1. Add agent");
            System.out.println("2. Update agent");
            System.out.println("3. Delete agent");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("First name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Type (Worker, Responsible, Director, Intern): ");
                    String type = scanner.nextLine();

                    List<Department> departments = departmentService.getAllDepartments();
                    System.out.println("Departments:");
                    departments.forEach(d -> System.out.println(d.getIdDepartment() + ". " + d.getName()));
                    System.out.print("Department ID: ");
                    int depId = Integer.parseInt(scanner.nextLine());
                    Department department = departmentService.getDepartmentById(depId);

                    Agent newAgent = new Agent();
                    newAgent.setFirstName(firstName);
                    newAgent.setLastName(lastName);
                    newAgent.setEmail(email);
                    newAgent.setPassword(password);
                    newAgent.setTypeAgent(type);
                    newAgent.setDepartment(department);

                    agentService.addAgent(newAgent);
                    System.out.println("Agent added successfully!");
                }
                case 2 -> updateAgent();
                case 3 -> deleteAgent();
            }
        } while (choice != 0);
    }

    private void updateAgent() {
        System.out.print("Enter agent ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Agent agent = agentService.getAgentById(id);
        if (agent == null) {
            System.out.println("Agent not found.");
            return;
        }
        System.out.print("New first name: ");
        agent.setFirstName(scanner.nextLine());
        System.out.print("New last name: ");
        agent.setLastName(scanner.nextLine());
        System.out.print("New email: ");
        agent.setEmail(scanner.nextLine());
        System.out.print("New password: ");
        agent.setPassword(scanner.nextLine());

        agentService.updateAgent(agent);
        System.out.println("Agent updated!");
    }

    private void deleteAgent() {
        System.out.print("Enter agent ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        agentService.deleteAgent(id);
        System.out.println("Agent deleted!");
    }

    private void addPayment(Agent loggedAgent) {
        System.out.print("Agent ID to add payment: ");
        int agentId = Integer.parseInt(scanner.nextLine());
        Agent agent = agentService.getAgentById(agentId);
        if (agent == null) {
            System.out.println("Agent not found.");
            return;
        }

        System.out.print("Payment type (Salary, Prime, Bonus, Indemnite): ");
        String type = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Reason: ");
        String reason = scanner.nextLine();

        Payment payment = new Payment();
        payment.setAgent(agent);
        payment.setPaymentType(type);
        payment.setAmount(amount);
        payment.setVerified(true);
        payment.setDate(LocalDateTime.now());
        payment.setReason(reason);

        String validationError = PaymentValidator.validate(payment);
        if (validationError != null) {
            System.out.println(validationError);
            return;
        }

        paymentService.addPayment(payment);
        System.out.println("Payment added successfully!");
    }

    private void viewPaymentsOfAgent() {
        System.out.print("Agent ID: ");
        int agentId = Integer.parseInt(scanner.nextLine());
        List<Payment> payments = paymentService.getPaymentsByAgent(agentId);
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        payments.forEach(this::printPayment);
    }

    private void manageDepartments() {
        int choice;
        do {
            System.out.println("\n--- Manage Departments ---");
            System.out.println("1. Add department");
            System.out.println("2. Update department");
            System.out.println("3. Delete department");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Department name: ");
                    String name = scanner.nextLine();
                    Department dep = new Department();
                    dep.setName(name);
                    departmentService.addDepartment(dep);
                    System.out.println("Department added!");
                }
                case 2 -> {
                    System.out.print("Department ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Department dep = departmentService.getDepartmentById(id);
                    if (dep == null) {
                        System.out.println("Department not found.");
                        return;
                    }
                    System.out.print("New name: ");
                    dep.setName(scanner.nextLine());
                    departmentService.updateDepartment(dep);
                    System.out.println("Department updated!");
                }
                case 3 -> {
                    System.out.print("Department ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    departmentService.deleteDepartment(id);
                    System.out.println("Department deleted!");
                }
            }
        } while (choice != 0);
    }

    // ---------------- Helper ----------------
    private void printPayment(Payment p) {
        System.out.println("Payment ID: " + p.getPaymentId() +
                ", Type: " + p.getPaymentType() +
                ", Amount: " + p.getAmount() +
                ", Verified: " + p.isVerified() +
                ", Date: " + p.getDate() +
                ", Reason: " + p.getReason());
    }
}

