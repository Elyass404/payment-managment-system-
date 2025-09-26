package org.dao.daoImpliment;

import org.dao.AgentDao;
import org.model.Agent;

import java.util.List;

import java.sql.*;

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
            stmnt.setString(6, agent.getDepartment().getIdDepartment());
        }catch(SQLException e){
            e.printStackTrace();
        };
    }

    public Agent getAgentById(int id){

        String sql = "SELECT * FROM agent WHERE agent_id = ?";
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {

            //here iam binding the parameters
            stmnt.setInt(1, id);
            
            //here iam binding the parameters
            ResultSet result = stmnt.executeQuery();

            if (result.next()) {
                // Build Agent object
                Agent agent = new Agent(
                        result.getString("agent_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("agent_type"),
                        //until we made the methods of getting the department or making the joins
                        null,
                        null
                );
                return agent;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Agent> getAllAgents(){
        return null;
    }

    public void updateAgent  (Agent agent){

    }

    public void deleteAgent(Agent agent){

    }




}


