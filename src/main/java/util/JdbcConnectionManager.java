package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {
    private JdbcConnectionManager() { }

    private static class Holder {
        private static final JdbcConnectionManager INSTANCE = new JdbcConnectionManager();
    }

    public static JdbcConnectionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        String url  = "jdbc:mysql://localhost:3306/payments_management?useSSL=false&serverTimezone=UTC";
        String user = "user";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }
}
