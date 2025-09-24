package org;
import com.mysql.cj.jdbc.Driver;
import util.JdbcConnectionManager;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        var conn = JdbcConnectionManager.getInstance().getConnection();
        System.out.println("The connection between the project and the database is done successfully: " + conn);
        conn.close();
    }
}