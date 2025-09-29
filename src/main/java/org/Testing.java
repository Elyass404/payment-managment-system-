package org;
import org.dao.PaymentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import util.JdbcConnectionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Testing {
    public static void main(String[] args){

        try {
            var conn = JdbcConnectionManager.getInstance().getConnection();
            System.out.println("The connection between the project and the database is done successfully: " + conn);
            //conn.close();

            DepartmentDaoImpl departmentDao = new DepartmentDaoImpl(conn);
            Department dep = new Department("RH");
            departmentDao.addDepartment(dep);

            Optional<Department> getDep = departmentDao.getDepartmentById(dep.getIdDepartment());
            getDep.ifPresent(d -> System.out.println("Department found: " + d.getName()));

            // Test AgentDao
            AgentDaoImpl agentDao = new AgentDaoImpl(conn);
            Agent agent = new Agent("Hamza", "test", "Hamza.test@example.com", "1234","Worker", dep);

            agentDao.addAgent(agent);


            //agent.setEmail("updated@email.com");
            agentDao.updateAgent(agent);

            dep.setName("RH");
            departmentDao.updateDepartment(dep);

            Optional<Agent> getAgent = agentDao.getAgentById(agent.getIdAgent());
            getAgent.ifPresent(a -> System.out.println("Agent found: "+a.getIdAgent()+" | " + a.getFirstName() + " " + a.getLastName() + " |" + a.getEmail()));

            List<Agent> allAgents = agentDao.getAllAgents();
            for(Agent a : allAgents){
                System.out.println("Agent N°: "+a.getIdAgent()+" | " + a.getFirstName() + " " + a.getLastName());
            }

            //testing the payment entity
            PaymentDao paydao = new PaymentDaoImpl(conn);
            Payment payment = new Payment( agent,  1200.00,  "Salary" ,  true, LocalDateTime.of(2025, 1, 15, 0, 0), "His salary of the month");

            paydao.addPayment(payment);
            paydao.getAllPayments().forEach(System.out::println);


            //teting the delete methods
//        agentDao.deleteAgent(agent);
//        departmentDao.deleteDepartment(dep);
//          paymentDao.deletePayment(payment);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}