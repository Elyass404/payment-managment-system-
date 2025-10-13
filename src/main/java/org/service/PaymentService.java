package org.service;

import org.model.Agent;
import org.model.Payment;
import java.util.List;

public interface PaymentService {

    //basic Crud methods
    String addPayment(Agent currentAgent, Payment payment);
    List<Payment> getPaymentsByAgent(int agentId);
    List<Payment> getAllPayments();
    boolean updatePayment(Agent currentAgent, Payment payment);
    boolean deletePayment(Agent currentAgent, Payment payment);

    //other methods to add
    double calculateTotalPaymentsByAgent(int agentId);
    List<Payment> filterPaymentsByType(int agentId, String type);
    List<Payment> sortPaymentsByDate(int agentId);
    List<Payment> sortPaymentsByAmount(int agentId);




    double calculateTotalPaymentsByDepartment(Agent currentAgent, int departmentId, List<Agent> agentsOfDepartment);
    double calculateAverageSalaryByDepartment(Agent currentAgent, int departmentId, List<Agent> agentsOfDepartment);
    List<Agent> rankAgentsByTotalPayment(Agent currentAgent, List<Agent> agentsOfDepartment);



    void AgentsAverageSalaryByDepartment(Agent currentAgent, int departmentId);
}
