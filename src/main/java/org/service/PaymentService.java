package org.service;

import org.model.Payment;
import java.util.List;

public interface PaymentService {

    //basic Crud methods
    void addPayment(Payment payment);
    List<Payment> getPaymentsByAgent(int agentId);
    List<Payment> getAllPayments();
    boolean updatePayment(Payment payment);
    boolean deletePayment(Payment payment);

    //other methods to add
    double calculateTotalPaymentsByAgent(int agentId);
    List<Payment> filterPaymentsByType(int agentId, String type);
    List<Payment> sortPaymentsByDate(int agentId);
}
