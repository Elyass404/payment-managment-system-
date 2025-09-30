package org.dao;

import org.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentDao {
    void addPayment(Payment payment);
    Optional<Payment> getPaymentById(int id);
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByAgentId(int agentId);
    boolean updatePayment(Payment payment);
    boolean deletePayment(Payment payment);
}
