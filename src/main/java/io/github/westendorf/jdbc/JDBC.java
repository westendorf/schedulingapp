package io.github.westendorf.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDBC {
    private Connection conn;

    /**
     * Setups a new connection to abstract away the low-level requirements for making it
     *
     * @return Connection
     */
    public Connection getNewConnection() {
        if(conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            try {
                String jdbcUrl = "jdbc:mysql://localhost/client_schedule?connectionTimeZone= SERVER ";
                String username = "sqlUser";
                String password = "Passw0rd!";
                conn = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new RuntimeException();
            }
        }
        return conn;
    }

    /**
     * This method safely closes the connection
     */
    public void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new RuntimeException("connection should close");
            }
        }
    }

    /**
     * The returns the last known connection, closed or not
     *
     * @return connection
     */
    public Connection getLastConnection() {
        return conn;
    }
}
