package org.model;

import java.util.List;

public class Department {
    private int idDepartment;
    private String name;
    private List<Agent> agents;

    //default constructor
    public Department(){};

    //parametrized constructor with agents list
    public Department(int idDepartment, String name, List<Agent> agents) {

        this.idDepartment = idDepartment;
        this.name = name;
        this.agents = agents;
    }

    //parametrized constructor with only the name of the department
    public Department(String name){
        this.name = name;
    }

    //Getters and Setters
    public int getIdDepartment() {
        return idDepartment;
    }

    public String getName() {
        return name;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setIdDepartment(int idDepartment) {
        this.idDepartment = idDepartment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
}
