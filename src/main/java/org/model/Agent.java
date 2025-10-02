package org.model;

import java.util.List;

public class Agent extends Person {

    private int idAgent;
    private String typeAgent;
    private Department department;
    private boolean isResponsible;
    private List<Payment> payments;

    // Default constructor (no args)
    public Agent() {}

    // Constructor with parent constructor
    public Agent(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email, password);
    }

    // Constructor without password
    public Agent(int idAgent, String firstName, String lastName, String email,
                 String typeAgent, Department department, List<Payment> payments) {
        super(firstName, lastName, email);
        this.idAgent = idAgent;
        this.typeAgent = typeAgent;
        this.department = department;
        this.payments = payments;
    }

    public Agent(String firstName, String lastName, String email, String password,
                 String typeAgent, Department department) {
        super(firstName, lastName, email, password);
        this.typeAgent = typeAgent;
        this.department = department;
    }

    // Parametrized constructor
    public Agent(int idAgent, String firstName, String lastName, String email, String password,
                 String typeAgent, Department department, List<Payment> payments) {
        super(firstName, lastName, email, password);
        this.idAgent = idAgent;
        this.typeAgent = typeAgent;
        this.department = department;
        this.payments = payments;
    }

    // Getters and setters
    public int getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }

    public String getTypeAgent() {
        return typeAgent;
    }

    public void setTypeAgent(String typeAgent) {
        this.typeAgent = typeAgent;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean getIsResponsible() {
        return isResponsible;
    }

    public void setIsResponsible(boolean isResponsible) {
        this.isResponsible = isResponsible;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "idAgent=" + idAgent +
                ", typeAgent='" + typeAgent + '\'' +
                ", department=" + department +
                ", isResponsible=" + isResponsible +
                ", Password = " + getPassword() +
                ", payments=" + payments +
                '}';
    }
}
