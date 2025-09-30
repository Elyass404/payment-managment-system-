package org.service.serviceImpl;

import org.dao.PaymentDao;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Payment;
import org.service.PaymentService;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;

    public PaymentServiceImpl(Connection connection) {
        this.paymentDao = new PaymentDaoImpl(connection);
    }

    @Override
    public void addPayment(Payment payment) {
        paymentDao.addPayment(payment);
    }

    @Override
    public List<Payment> getPaymentsByAgent(int agentId) {
        return paymentDao.getPaymentsByAgentId(agentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDao.getAllPayments();
    }

    @Override
    public boolean updatePayment(Payment payment) {
        return paymentDao.updatePayment(payment);

    }

    @Override
    public boolean deletePayment(Payment payment) {
        return paymentDao.deletePayment(payment);

    }

    // ---------- Business logic methods ----------
    @Override
    public double calculateTotalPaymentsByAgent(int agentId) {
        return getPaymentsByAgent(agentId)
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public List<Payment> filterPaymentsByType(int agentId, String type) {
        return getPaymentsByAgent(agentId)
                .stream()
                .filter(p -> p.getPaymentType().equalsIgnoreCase(type))
                .toList();
    }

    @Override
    public List<Payment> sortPaymentsByDate(int agentId) {
        return getPaymentsByAgent(agentId)
                .stream()
                .sorted(Comparator.comparing(Payment::getDate))
                .toList();
    }
}
