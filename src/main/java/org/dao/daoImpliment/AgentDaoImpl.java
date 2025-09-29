package org.dao.daoImpliment;

import org.dao.AgentDao;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.Optional;

public class AgentDaoImpl implements AgentDao {

    private Connection connection;


    public AgentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addAgent(Agent agent){

        String sql = "INSERT INTO agent(first_name,last_name, email, password, agent_type, department_id) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement  stmnt = connection.prepareStatement(sql)){
            stmnt.setString(1, agent.getFirstName());
            stmnt.setString(2, agent.getLastName());
            stmnt.setString(3, agent.getEmail());
            stmnt.setString(4, agent.getPassword());
            stmnt.setString(5, agent.getTypeAgent());
            stmnt.setInt(6, agent.getDepartment().getIdDepartment());

            int result = stmnt.executeUpdate();
            if(result > 0){
                System.out.println("You successfully added a new agent.");
            }else{
                System.out.println("Sorry, something went wrong when adding new agent, please try again.");
            }

        }catch(SQLException e){
            e.printStackTrace();
        };
    }

    @Override
    public Optional<Agent> getAgentById(int id){

        String sql = "SELECT * FROM agent WHERE agent_id = ?";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {

            //here iam binding the parameters
            stmnt.setInt(1, id);
            //here iam binding the parameters
            ResultSet result = stmnt.executeQuery();

            if (result.next()) {

                int departmentId = result.getInt("department_id");
                Department department = new DepartmentDaoImpl(connection)
                        .getDepartmentById(departmentId)
                        .orElse(null);

                // making an agent instance without the pass word
                Agent agent = new Agent(
                        result.getInt("agent_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("email"),
                        result.getString("agent_type"),

                        department,
                        //we will keep the payments null at first, because we need the agent id to get payments
                        null
                );

                List<Payment> payments = new PaymentDaoImpl(connection)
                        .getPaymentsByAgentId(agent.getIdAgent());
                agent.setPayments(payments); //now we got the agent payments

                return Optional.of(agent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Agent> getAllAgents(){
        String sql = "Select * from agent";
        List<Agent> agents = new ArrayList<>();

        try(PreparedStatement stmnt = connection.prepareStatement(sql)){

            ResultSet result = stmnt.executeQuery();
            while(result.next()){

                int departmentId = result.getInt("department_id");
                Department department = new DepartmentDaoImpl(connection)
                        .getDepartmentById(departmentId)
                        .orElse(null);

                Agent agent = new Agent(
                        result.getInt("agent_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("email"),
                        result.getString("agent_type"),

                        department,
                        //we will keep the payments null at first, because we need the agent id to get payments
                        null
                );

                List<Payment> payments = new PaymentDaoImpl(connection)
                        .getPaymentsByAgentId(agent.getIdAgent());
                agent.setPayments(payments);

                agents.add(agent);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public void updateAgent  (Agent agent){

        String  sql ="UPDATE agent set " +
                "first_name = ? , " +
                "last_name = ? , " +
                "email = ? , " +
                "password = ? , " +
                "agent_type = ? , " +
                "department_id = ?  " +
                "WHERE agent_id = ?";

        try(PreparedStatement stmnt = connection.prepareStatement(sql)){
            stmnt.setString(1,agent.getFirstName());
            stmnt.setString(2,agent.getLastName());
            stmnt.setString(3,agent.getEmail());
            stmnt.setString(4,agent.getPassword());
            stmnt.setString(5,agent.getTypeAgent());
            stmnt.setInt(6,agent.getDepartment().getIdDepartment());
            stmnt.setInt(7,agent.getIdAgent());

            int result = stmnt.executeUpdate();
            if(result > 0 ){
                System.out.println("You have updated the agent with success!");
            }else{
                System.out.println("Your update has been failed!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAgent(Agent agent){

        String sql ="DELETE FROM agent WHERE agent_id = ?";

        try(PreparedStatement stmnt = connection.prepareStatement(sql)){

            stmnt.setInt(1, agent.getIdAgent());
            int result = stmnt.executeUpdate();

            if(result > 0){
                System.out.println("You have deleted the agent with success!");
            }else {
                System.out.println("Your delete failed!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}


