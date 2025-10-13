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
    public String addPayment(Agent currentAgent, Payment payment) {
        System.out.println("yyyyyyyyyyyyyyyyyyyyy");
        System.out.println(payment.getDate().getYear());
        if (!agentService.isDirector(currentAgent)) {  // if he is not a director
            if (!agentService.isResponsible(currentAgent)) {// if not director we will check if he is responsibel
                throw new SecurityException("Only Director or Responsible can add payments.");
            } else {  // now if he is a responsible then
                if (payment.getAgent() == null || payment.getAgent().getDepartment() == null) {
                    throw new IllegalArgumentException("Payment must be linked to an agent with a department.");
                }

                if (payment.getAgent().getDepartment().getIdDepartment() !=
                        currentAgent.getDepartment().getIdDepartment()) {
                    throw new SecurityException("Responsible can only add payments for agents in their own department.");
                }

                String validationError = PaymentValidator.validate(payment);
                if (validationError != null) {
                    return validationError;
                }

                paymentDao.addPayment(payment);
                return "Payment added successfully by Responsible!";
            }
        } else { //but if he is a director then
            String validationError = PaymentValidator.validate(payment);
            if (validationError != null) {
                return validationError;
            }

            paymentDao.addPayment(payment);
            return "Payment added successfully by Director!";
        }
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
    public boolean updatePayment(Agent currentAgent, Payment payment) {

        if (!agentService.isDirector(currentAgent)) {  // not director
            if (!agentService.isResponsible(currentAgent)) {
                throw new SecurityException("Only Director or Responsible can update payments.");
            } else { // Responsible case
                if (payment.getAgent() == null || payment.getAgent().getDepartment() == null) {
                    throw new IllegalArgumentException("Payment must be linked to an agent with a department.");
                }

                if (payment.getAgent().getDepartment().getIdDepartment() !=
                        currentAgent.getDepartment().getIdDepartment()) {
                    throw new SecurityException("Responsible can only update payments for agents in their own department.");
                }

                String validationError = PaymentValidator.validate(payment);
                if (validationError != null) {
                    System.out.println("Validation failed: " + validationError);
                    return false;
                }

                return paymentDao.updatePayment(payment);
            }
        } else { // Director case
            String validationError = PaymentValidator.validate(payment);
            if (validationError != null) {
                System.out.println("Validation failed: " + validationError);
                return false;
            }

            return paymentDao.updatePayment(payment);
        }
    }

    @Override
    public boolean deletePayment(Agent currentAgent, Payment payment) {
        if (!agentService.isDirector(currentAgent)) { // not director
            if (!agentService.isResponsible(currentAgent)) {
                throw new SecurityException("Only Director or Responsible can delete payments.");
            } else { // Responsible case
                if (payment.getAgent() == null || payment.getAgent().getDepartment() == null) {
                    throw new IllegalArgumentException("Payment must be linked to an agent with a department.");
                }

                if (payment.getAgent().getDepartment().getIdDepartment() !=
                        currentAgent.getDepartment().getIdDepartment()) {
                    throw new SecurityException("Responsible can only delete payments for agents in their own department.");
                }

                return paymentDao.deletePayment(payment);
            }
        } else { // Director case
            return paymentDao.deletePayment(payment);
        }
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
    public double calculateTotalPaymentsByDepartment(Agent currentAgent, int departmentId, List<Agent> agentsOfDepartment) {
        if (!agentService.isDirector(currentAgent)) {
            if (!agentService.isResponsible(currentAgent)) {
                throw new SecurityException("Only Director or Responsible can view department payments.");
            } else { // Responsible case
                if (currentAgent.getDepartment() == null ||
                        currentAgent.getDepartment().getIdDepartment() != departmentId) {
                    throw new SecurityException("Responsible can only view stats for their own department.");
                }
            }
        }

        return agentsOfDepartment.stream()
                .flatMap(agent -> getPaymentsByAgent(agent.getIdAgent()).stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public double calculateAverageSalaryByDepartment(Agent currentAgent, int departmentId, List<Agent> agentsOfDepartment) {
        if (!agentService.isDirector(currentAgent)) {
            if (!agentService.isResponsible(currentAgent)) {
                throw new SecurityException("Only Director or Responsible can view salary stats.");
            } else { // Responsible case
                if (currentAgent.getDepartment() == null ||
                        currentAgent.getDepartment().getIdDepartment() != departmentId) {
                    throw new SecurityException("Responsible can only view stats for their own department.");
                }
            }
        }

        return agentsOfDepartment.stream()
                .flatMap(agent -> filterPaymentsByType(agent.getIdAgent(), "Salary").stream())
                .mapToDouble(Payment::getAmount)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<Agent> rankAgentsByTotalPayment(Agent currentAgent, List<Agent> agentsOfDepartment) {
        if (!agentService.isDirector(currentAgent)) {
            if (!agentService.isResponsible(currentAgent)) {
                throw new SecurityException("Only Director or Responsible can rank agents by payment.");
            } else { // Responsible case
                if (currentAgent.getDepartment() == null ||
                        agentsOfDepartment.stream().noneMatch(a ->
                                a.getDepartment() != null &&
                                        a.getDepartment().getIdDepartment() == currentAgent.getDepartment().getIdDepartment()
                        )) {
                    throw new SecurityException("Responsible can only rank agents within their own department.");
                }
            }
        }

        return agentsOfDepartment.stream()
                .sorted((a1, a2) -> Double.compare(
                        calculateTotalPaymentsByAgent(a2.getIdAgent()),
                        calculateTotalPaymentsByAgent(a1.getIdAgent())
                ))
                .toList();
    }

    @Override
    public void AgentsAverageSalaryByDepartment(Agent currentAgent, int departmentId){
        List <Agent> agentsByDepartment = agentService.getAgentsByDepartment(currentAgent,departmentId);
        agentsByDepartment.stream()
                .forEach(agent -> {
                    System.out.println(agent.getFirstName());
                    double average=agent.getPayments().stream().mapToDouble(Payment::getAmount).average().orElse(0);
                    System.out.println("average"+average);
                })
                ;
    }

}
