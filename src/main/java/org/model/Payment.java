package org.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private int paymentId;
    private Agent agent;
    private double amount;
    private String paymentType;
    private boolean verified;
    private LocalDateTime date;
    private String reason;

    // Constructors

    // Full constructor
    public Payment(int paymentId, Agent agent, double amount, String paymentType, boolean verified, LocalDateTime date, String reason) {
        this.paymentId = paymentId;
        this.agent = agent;
        this.amount = amount;
        this.paymentType = paymentType;
        this.verified = verified;
        this.date = date;
        this.reason = reason;
    }

    // Constructor without paymentId (for inserting new payments)
    public Payment(Agent agent, double amount, String paymentType, boolean verified, LocalDateTime date, String reason) {
        this.agent = agent;
        this.amount = amount;
        this.paymentType = paymentType;
        this.verified = verified;
        this.date = date;
        this.reason = reason;
    }

    // default constructor
    public Payment() {}

    // Getters and Setters

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
