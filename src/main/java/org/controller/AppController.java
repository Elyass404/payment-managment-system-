package org.controller;

import org.model.Agent;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;
import org.view.DirectorMenu;
import org.view.ResponsibleMenu;
import org.view.WorkerMenu;

import java.util.Scanner;

public class AppController {

    private final AgentService agentService;
    private final PaymentService paymentService;
    private final DepartmentService departmentService;
    private final Scanner scanner;

    private Agent currentAgent; // logged-in agent

    public AppController(AgentService agentService, PaymentService paymentService, DepartmentService departmentService) {
        this.agentService = agentService;
        this.paymentService = paymentService;
        this.departmentService = departmentService;
        this.scanner = new Scanner(System.in);
    }

    // ---------- Login ----------
    public void login() {
        System.out.println("==== WELCOME TO PAYMENTS MANAGEMENT SYSTEM ====");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            currentAgent = agentService.login(email, password);
            if (currentAgent == null) {
                System.out.println("Invalid credentials. Try again.");
            } else {
                loggedIn = true;
                System.out.println("Welcome, " + currentAgent.getFirstName() + " (" + currentAgent.getTypeAgent() + ")");
                redirectToMenu();
            }
        }
    }

    // ---------- Redirect to role menu ----------
    private void redirectToMenu() {
        String type = currentAgent.getTypeAgent();

        switch (type) {
            case "Director" -> new DirectorMenu(currentAgent, agentService, paymentService, departmentService).showMenu();
            case "Responsible" -> new ResponsibleMenu(currentAgent, agentService, paymentService, departmentService).showMenu();
            case "Worker" -> new WorkerMenu(currentAgent, paymentService).showMenu();
            default -> System.out.println("Unknown agent type. Exiting...");
        }
    }

    public Scanner getScanner() {
        return this.scanner;
    }


}
