package org.dao.daoImpliment;

import org.dao.PaymentDao;
import org.model.Agent;
import org.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDaoImpl implements PaymentDao {

    private Connection connection;

    public PaymentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addPayment(Payment payment) {
        String sql = "INSERT INTO payment(agent_id, amount, payment_type, verified, date, reason) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, payment.getAgent().getIdAgent());
            stmnt.setBigDecimal(2, payment.getAmount());
            stmnt.setString(3, payment.getPaymentType());
            stmnt.setBoolean(4, payment.isVerified());
            stmnt.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            stmnt.setString(6, payment.getReason());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Payment added successfully!");
            } else {
                System.out.println("Failed to add payment, please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Payment> getPaymentById(int id) {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, id);

            try (ResultSet result = stmnt.executeQuery()) {
                if (result.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(result.getInt("payment_id"));

                    int agentId = result.getInt("agent_id");
                    Agent agent = new AgentDaoImpl(connection).getAgentById(agentId).orElse(null);
                    payment.setAgent(agent);

                    payment.setAmount(result.getBigDecimal("amount"));
                    payment.setPaymentType(result.getString("payment_type"));
                    payment.setVerified(result.getBoolean("verified"));
                    payment.setDate(result.getTimestamp("payment_date").toLocalDateTime());
                    payment.setReason(result.getString("reason"));

                    return Optional.of(payment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Payment> getAllPayments() {
        String sql = "SELECT * FROM payment";
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement stmnt = connection.prepareStatement(sql);
             ResultSet rs = stmnt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));

                // Fetch agent using AgentDao
                int agentId = rs.getInt("agent_id");
                Agent agent = new AgentDaoImpl(connection).getAgentById(agentId).orElse(null);
                payment.setAgent(agent);

                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentType(rs.getString("payment_type"));
                payment.setVerified(rs.getBoolean("verified"));
                payment.setDate(rs.getTimestamp("payment_date").toLocalDateTime());
                payment.setReason(rs.getString("reason"));

                payments.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> getPaymentsByAgentId(int agentId) {
        String sql = "SELECT * FROM payment WHERE agent_id = ?";
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, agentId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));

                    // Fetch the agent (optional: you can reuse agentId directly)
                    Agent agent = new AgentDaoImpl(connection).getAgentById(agentId).orElse(null);
                    payment.setAgent(agent);

                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setPaymentType(rs.getString("payment_type"));
                    payment.setVerified(rs.getBoolean("verified"));
                    payment.setDate(rs.getTimestamp("payment_date").toLocalDateTime());
                    payment.setReason(rs.getString("reason"));

                    payments.add(payment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    @Override
    public void updatePayment(Payment payment) {
        String sql = "UPDATE payment SET agent_id = ?, amount = ?, payment_type = ?, verified = ?, payment_date = ?, reason = ? WHERE payment_id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, payment.getAgent().getIdAgent());
            stmnt.setBigDecimal(2, payment.getAmount());
            stmnt.setString(3, payment.getPaymentType());
            stmnt.setBoolean(4, payment.isVerified());
            stmnt.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            stmnt.setString(6, payment.getReason());
            stmnt.setInt(7, payment.getPaymentId());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Payment updated successfully!");
            } else {
                System.out.println("Failed to update payment. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deletePayment(Payment payment) {
        String sql = "DELETE FROM payment WHERE payment_id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, payment.getPaymentId());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Payment deleted successfully!");
            } else {
                System.out.println("Failed to delete payment. Please check the ID and try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
