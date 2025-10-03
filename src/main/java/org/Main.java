package org;

import org.controller.AppController;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;
import org.service.serviceImpl.AgentServiceImpl;
import org.service.serviceImpl.DepartmentServiceImpl;
import org.service.serviceImpl.PaymentServiceImpl;
import util.JdbcConnectionManager;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println(" Welcome to the Payments Management System! ");
        System.out.println("===================================");

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {

            // ----- Initialize Services -----
            AgentService agentService = new AgentServiceImpl(connection);
            DepartmentService departmentService = new DepartmentServiceImpl(connection, agentService);
            PaymentService paymentService = new PaymentServiceImpl(connection, agentService);

            // ----- Initialize Controller -----
            AppController appController = new AppController(agentService, paymentService, departmentService);

            boolean exit = false;

            while (!exit) {
                // ---- Login and redirect to role menu ----
                appController.login(); // loops until correct credentials

                // After user logs out from menu, ask if they want to exit
                System.out.println("\nDo you want to exit the program? (yes/no)");
                String input = appController.getScanner().nextLine(); // get Scanner from controller
                if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                    exit = true;
                    System.out.println("Goodbye!");
                }
            }

        } catch (Exception e) {
            System.out.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
