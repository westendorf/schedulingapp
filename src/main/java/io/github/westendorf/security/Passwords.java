package io.github.westendorf.security;

import io.github.westendorf.jdbc.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Passwords {
    /**
     * Checks the validity of a username and password
     *
     * @param username
     * @param password
     * @return Match or not
     */
    public static boolean checkPassword(String username, String password) {
        boolean matches = false;
        String sql = "SELECT * FROM users WHERE User_Name=? AND Password=?";
        JDBC jdbc = new JDBC();
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            matches = rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            jdbc.closeConnection();
        }
        return matches;
    }
}
