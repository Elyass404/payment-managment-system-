package org.service.serviceImpl;

import org.dao.PaymentDao;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Payment;
import org.service.AgentService;
import org.service.PaymentService;
import util.PaymentValidator;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;
    private final AgentService agentService;

    public PaymentServiceImpl(Connection connection, AgentService agentService) {
        this.paymentDao = new PaymentDaoImpl(connection);
        this.agentService = agentService;
    }


    @Override
    public String addPayment(Payment payment) {

        // we should validate the payment first using the validator
        String validationError = PaymentValidator.validate(payment);
        if (validationError != null) {
            return validationError; // don’t insert, return the error msg
        }
        paymentDao.addPayment(payment);
        return "Payment added successfully!";
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

    @Override
    public List<Payment> sortPaymentsByAmount(int agentId) {
        List<Payment> payments = getPaymentsByAgent(agentId);

        if (payments == null || payments.isEmpty()) {
            System.out.println("No payments found for agent with ID: " + agentId);
            return List.of();
        }

        return payments.stream()
                .sorted((p1, p2) -> Double.compare(p2.getAmount(), p1.getAmount()))
                .toList();
    }

    @Override
    public double calculateTotalPaymentsByDepartment(int departmentId, List<Agent> agentsOfDepartment) {
        return agentsOfDepartment.stream()
                .flatMap(agent -> getPaymentsByAgent(agent.getIdAgent()).stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public double calculateAverageSalaryByDepartment(int departmentId, List<Agent> agentsOfDepartment) {
        return agentsOfDepartment.stream()
                .flatMap(agent -> filterPaymentsByType(agent.getIdAgent(), "SALARY").stream())
                .mapToDouble(Payment::getAmount)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<Agent> rankAgentsByTotalPayment(List<Agent> agentsOfDepartment) {
        return agentsOfDepartment.stream()
                .sorted((a1, a2) -> Double.compare(
                        calculateTotalPaymentsByAgent(a2.getIdAgent()),
                        calculateTotalPaymentsByAgent(a1.getIdAgent())
                ))
                .toList();
    }


}
