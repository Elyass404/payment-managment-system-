package org.view;

import org.model.Agent;
import org.model.Payment;
import org.service.PaymentService;

import java.util.List;
import java.util.Scanner;

public class WorkerMenu {

    private final Agent currentAgent;
    private final PaymentService paymentService;
    private final Scanner scanner;

    public WorkerMenu(Agent currentAgent, PaymentService paymentService) {
        this.currentAgent = currentAgent;
        this.paymentService = paymentService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== WORKER MENU ====");
            System.out.println("1. View Personal Info");
            System.out.println("2. My Payments History");
            System.out.println("3. Calculate Total My Payments");
            System.out.println("4. Logout");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewPersonalInfo();
                case "2" -> viewMyPayments();
                case "3" -> calculateMyTotalPayments();
                case "4" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

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
            System.out.println("Payment ID | Type | Amount");
            for (Payment p : payments) {
                System.out.println(p.getPaymentId() + " | " + p.getPaymentType() + " | " + p.getAmount());
            }
        }
    }

    private void calculateMyTotalPayments() {
        double total = paymentService.calculateTotalPaymentsByAgent(currentAgent.getIdAgent());
        System.out.println("Total payments: " + total);
    }
}
