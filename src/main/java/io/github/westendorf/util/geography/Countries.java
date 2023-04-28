package io.github.westendorf.util.geography;

import io.github.westendorf.jdbc.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Countries {

    /**
     * Fetches the ID of a country given its name from the database
     *
     * @param country
     * @return ID of country
     */
    public static int getId(String country) {
        int id = 0;
        String sql = "SELECT Country_ID FROM countries WHERE Country=?";
        JDBC jdbc = new JDBC();
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt("Country_ID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            jdbc.closeConnection();
        }
        return id;
    }

    /**
     * Fetches all countries from the database
     *
     * @return List of countries
     */
    public static List<String> getAll() {
        List<String> countries = new ArrayList<>();
        JDBC jdbc = new JDBC();
        String sql = "SELECT Country FROM countries;";
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                countries.add(rs.getString("Country"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            jdbc.closeConnection();
        }

        return countries;
    }

    /**
     * Fetches a country from the database given a division ID
     *
     * @param divisionID
     * @return Country
     */
    public static String getFromDivisionId(int divisionID) {
        String country = "";
        JDBC jdbc = new JDBC();
        String sql = "SELECT Country FROM countries LEFT JOIN first_level_divisions ON countries.Country_ID=first_level_divisions.Country_ID WHERE Division_ID=?";
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) country = rs.getString("Country");
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return country;
    }
}
