package org.model;

import java.util.List;

public class Agent extends Person {

    private String idAgent;
    private String typeAgent;
    private Department department;
    private List<Payment> payments;

    //default constructor
    public Agent(String firstname, String lastname, String email, String password){
        super(firstname, lastname, email, password);
    };

    //Parametrized Constructor
    public Agent(String idAgent, String firstName, String lastName, String email, String password,
                 String typeAgent, Department department, List<Payment> payments) {

        super(firstName, lastName, email, password);
        this.idAgent= idAgent;
        this.typeAgent = typeAgent;
        this.department = department;
        this.payments = payments;
    }

    //Getters and setters

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
