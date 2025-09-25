package org.model;

import java.util.List;

public class Department {
    String idDepartment;
    String name;
    List<Agent> agents;

    //default constructor
    public Department(){};

    //parametrized constructor
    public Department(String idDepartment, String name, List<Agent> agents) {

        this.idDepartment = idDepartment;
        this.name = name;
        this.agents = agents;
    }

    //Getters and Setters 
    public String getIdDepartment() {
        return idDepartment;
    }

    public String getName() {
        return name;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
}
