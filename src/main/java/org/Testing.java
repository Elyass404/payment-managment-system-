package org;
import org.dao.PaymentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import org.service.DepartmentService;
import org.service.serviceImpl.DepartmentServiceImpl;
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
            //testing the payment entity
            PaymentDao paydao = new PaymentDaoImpl(conn);
            Payment payment = new Payment( agent,  1200.00,  "Salary" ,  true, LocalDateTime.of(2025, 1, 15, 0, 0), "His salary of the month");

            paydao.addPayment(payment);
            //paydao.getPaymentsByAgentId(17);
            //paydao.getPaymentById(4);
            paydao.getAllPayments();
            //paydao.getPaymentsByAgentId(17);


            //teting the delete methods
//        agentDao.deleteAgent(agent);
//        departmentDao.deleteDepartment(dep);
//          paymentDao.deletePayment(payment);

            //________________________________________________

            //testing the implimentation of the services

            DepartmentService departmentService =  new DepartmentServiceImpl(conn);

            // Add new department
            Department depart = new Department("Finance");
            departmentService.addDepartment(dep);

            // Fetch department by ID
            Department fetchedDep = departmentService.getDepartmentById(depart.getIdDepartment());
            if (fetchedDep != null) {
                System.out.println("Department found: " + fetchedDep.getName());
            }

            // Update department
            depart.setName("Accounting");
            departmentService.updateDepartment(depart);

            // Get all departments
            List<Department> allDepartments = departmentService.getAllDepartments();
            for (Department d : allDepartments) {
                System.out.println("Department: " + d.getIdDepartment() + " | " + d.getName());
            }

            // Delete department
            departmentService.deleteDepartment(depart.getIdDepartment());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}