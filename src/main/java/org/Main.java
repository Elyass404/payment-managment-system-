package org;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.AgentService;
import org.service.DepartmentService;
import org.service.PaymentService;
import org.service.serviceImpl.AgentServiceImpl;
import org.service.serviceImpl.DepartmentServiceImpl;
import org.service.serviceImpl.PaymentServiceImpl;
import util.JdbcConnectionManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args){

        try {
            Connection conn = JdbcConnectionManager.getInstance().getConnection();
            System.out.println("Database connected: " + conn);

            PaymentService paymentService = new PaymentServiceImpl(conn, null);

            Agent agent = new Agent();
            agent.setIdAgent(12);


            Payment payment = new Payment();
            payment.setPaymentType("Salary");
            payment.setAmount(2500.0);
            payment.setDate(LocalDateTime.now());
            payment.setReason("Test salary payment");
            payment.setAgent(agent);

            paymentService.addPayment(payment);

            // 3. Call the method
            List<Payment> sortedPayments = paymentService.sortPaymentsByAmount(agent.getIdAgent());

            // 4. Print results
            System.out.println("Payments sorted by amount (biggest → smallest):");
            sortedPayments.forEach(p -> System.out.println(
                    "ID: " + p.getPaymentId() +
                            ", Type: " + p.getPaymentType() +
                            ", Amount: " + p.getAmount()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}