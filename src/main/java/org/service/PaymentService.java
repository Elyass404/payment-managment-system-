package org.service;

import org.model.Agent;
import org.model.Payment;
import java.util.List;

public interface PaymentService {

    //basic Crud methods
    String addPayment(Payment payment);
    List<Payment> getPaymentsByAgent(int agentId);
    List<Payment> getAllPayments();
    boolean updatePayment(Payment payment);
    boolean deletePayment(Payment payment);

    //other methods to add
    double calculateTotalPaymentsByAgent(int agentId);
    List<Payment> filterPaymentsByType(int agentId, String type);
    List<Payment> sortPaymentsByDate(int agentId);
    List<Payment> sortPaymentsByAmount(int agentId);


    double calculateTotalPaymentsByDepartment(int departmentId, List<Agent> agentsOfDepartment);
    double calculateAverageSalaryByDepartment(int departmentId, List<Agent> agentsOfDepartment);
    List<Agent> rankAgentsByTotalPayment(List<Agent> agentsOfDepartment);



}
