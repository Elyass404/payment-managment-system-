package org.dao.daoImpliment;

import com.mysql.cj.protocol.Resultset;
import org.dao.DepartmentDao;
import org.dao.PaymentDao;
import org.model.Agent;
import org.model.Department;
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
        String sql = "INSERT INTO payment(agent_id, amount, payment_type, verified, payment_date, reason) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stmnt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmnt.setInt(1, payment.getAgent().getIdAgent());
            stmnt.setDouble(2, payment.getAmount());
            stmnt.setString(3, payment.getPaymentType());
            stmnt.setBoolean(4, payment.isVerified());
            stmnt.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            stmnt.setString(6, payment.getReason());

            int result = stmnt.executeUpdate();
            if (result > 0) {

                ResultSet idPaymnet = stmnt.getGeneratedKeys();
                if(idPaymnet.next()){
                    payment.setPaymentId(idPaymnet.getInt(1));
                }
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

                    payment.setAmount(result.getDouble("amount"));
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
        String sql = "SELECT * FROM payment join agent on agent.agent_id = payment.agent_id";
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement stmnt = connection.prepareStatement(sql);
             ResultSet result = stmnt.executeQuery()) {

            while (result.next()) {

                Agent agent = new Agent();

                agent.setIdAgent(result.getInt("agent_id"));
                agent.setFirstName(result.getString("first_name"));
                agent.setLastName(result.getString("last_name"));
                agent.setEmail(result.getString("email"));
                agent.setPassword(result.getString("password"));
                agent.setTypeAgent(result.getString("agent_type"));

                //Now lets make the payment object
                Payment payment = new Payment();

                payment.setPaymentId(result.getInt("payment_id"));
                payment.setAmount(result.getDouble("amount"));
                payment.setAgent(agent);
                payment.setPaymentType(result.getString("payment_type"));
                payment.setVerified(result.getBoolean("verified"));
                payment.setDate(result.getTimestamp("payment_date").toLocalDateTime());
                payment.setReason(result.getString("reason"));

                payments.add(payment);

            }

            for (Payment p : payments) {
                System.out.println("Payment ID: " + p.getPaymentId());
                System.out.println("Agent: " + p.getAgent().getFirstName() + " " + p.getAgent().getLastName());
                System.out.println("Amount: " + p.getAmount());
                System.out.println("Agent Type: " + p.getPaymentType());
                System.out.println("Verified: " + p.isVerified());
                System.out.println("Date: " + p.getDate());
                System.out.println("Reason: " + p.getReason());
                System.out.println("----------------------");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }


    @Override
    public List<Payment> getPaymentsByAgentId(int agentId) {
        String sql = "SELECT p.payment_id, p.amount, p.payment_type, p.verified, p.payment_date, p.reason, " +
                " a.agent_id, a.first_name, a.last_name, a.email, a.password, a.agent_type "  +
                " FROM payment p " +
                " JOIN agent a ON p.agent_id = a.agent_id " +
                " WHERE p.agent_id = ? ";


        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, agentId);

            try (ResultSet result = stmnt.executeQuery()) {
                while (result.next()) {

                    //let's build  the object of agent first
                    Agent agent = new Agent();
                    agent.setIdAgent(result.getInt("agent_id"));
                    agent.setFirstName(result.getString("first_name"));
                    agent.setLastName(result.getString("last_name"));
                    agent.setEmail(result.getString("email"));
                    agent.setPassword(result.getString("password"));
                    agent.setTypeAgent(result.getString("agent_type"));

                    //let's build now the object of payment and integrate the agent built in it

                    Payment payment = new Payment();
                    payment.setPaymentId(result.getInt("payment_id"));
                    payment.setAmount(result.getDouble("amount"));
                    payment.setAgent(agent);
                    payment.setPaymentType(result.getString("payment_type"));
                    payment.setVerified(result.getBoolean("verified"));
                    payment.setDate(result.getTimestamp("payment_date").toLocalDateTime());
                    payment.setReason(result.getString("reason"));

                    payments.add(payment);

                }
                for (Payment p : payments) {
                    System.out.println("Payment ID: " + p.getPaymentId());
                    System.out.println("Agent: " + p.getAgent().getFirstName() + " " + p.getAgent().getLastName());
                    System.out.println("Amount: " + p.getAmount());
                    System.out.println("Agent Type: " + p.getPaymentType());
                    System.out.println("Verified: " + p.isVerified());
                    System.out.println("Date: " + p.getDate());
                    System.out.println("Reason: " + p.getReason());
                    System.out.println("----------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payment SET agent_id = ?, amount = ?, payment_type = ?, verified = ?, payment_date = ?, reason = ? WHERE payment_id = ?";
        boolean decesion ;
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, payment.getAgent().getIdAgent());
            stmnt.setDouble(2, payment.getAmount());
            stmnt.setString(3, payment.getPaymentType());
            stmnt.setBoolean(4, payment.isVerified());
            stmnt.setTimestamp(5, Timestamp.valueOf(payment.getDate()));
            stmnt.setString(6, payment.getReason());
            stmnt.setInt(7, payment.getPaymentId());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Payment updated successfully!");
                return true;
            } else {
                System.out.println("Failed to update payment. Please try again.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deletePayment(Payment payment) {
        String sql = "DELETE FROM payment WHERE payment_id = ?";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, payment.getPaymentId());

            int result = stmnt.executeUpdate();
            if (result > 0) {
                System.out.println("Payment deleted successfully!");
                return true;
            } else {
                System.out.println("Failed to delete payment. Please check the ID and try again.");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }



}
