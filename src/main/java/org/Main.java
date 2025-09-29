package org;
import com.mysql.cj.jdbc.Driver;
import org.dao.AgentDao;
import org.dao.DepartmentDao;
import org.dao.PaymentDao;
import org.dao.daoImpliment.AgentDaoImpl;
import org.dao.daoImpliment.DepartmentDaoImpl;
import org.dao.daoImpliment.PaymentDaoImpl;
import org.model.Agent;
import org.model.Department;
import org.model.Payment;
import util.JdbcConnectionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args){

        try {
        var conn = JdbcConnectionManager.getInstance().getConnection();
        System.out.println("The connection between the project and the database is done successfully: " + conn);
        //conn.close();

            DepartmentDao departmentDao = new DepartmentDaoImpl(conn);
           Department dep = new Department("Depart");
            departmentDao.addDepartment(dep);

           AgentDao agentDao = new AgentDaoImpl(conn);
            Agent agent = new Agent("Ahmad", "test", "ahmad.test@example.com", "1234","Worker", dep);
            agentDao.addAgent(agent);


            //testing the payment entity
            PaymentDao paydao = new PaymentDaoImpl(conn);
            Payment payment = new Payment( agent,  1200.00,  "Salary" ,  true, LocalDateTime.of(2025, 1, 15, 0, 0), "His salary of the month");

            paydao.addPayment(payment);
            //paydao.getPaymentsByAgentId(17);
            //paydao.getPaymentById(4);
            paydao.getAllPayments();
            //paydao.getPaymentsByAgentId(17);

        } catch (Exception e){
        e.printStackTrace();
    }
}
}