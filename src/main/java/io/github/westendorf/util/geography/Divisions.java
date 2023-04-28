package io.github.westendorf.util.geography;

import io.github.westendorf.jdbc.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Divisions {
    /**
     * Fetches divisions from the database given a country
     *
     * @param country
     * @return List of divisions in String format
     */
    public static List<String> getAllByCountry(String country) {
        List<String> flds = new ArrayList<>();
        JDBC jdbc = new JDBC();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID=?";
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, Countries.getId(country));
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                flds.add(rs.getString("Division"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            jdbc.closeConnection();
        }

        return flds;
    }

    /**
     * Fetches the ID of a division given the country and the division name
     *
     * @param country
     * @param division
     * @return ID of division
     */
    public static int getId(String country, String division) {
        int id = 0;
        JDBC jdbc = new JDBC();
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division=? AND Country_ID=(SELECT Country_ID FROM countries WHERE Country=?)";
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setString(1, division);
            ps.setString(2, country);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) id = rs.getInt("Division_ID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            jdbc.closeConnection();
        }
        return id;
    }

    /**
     * Gets a division by an ID
     *
     * @param divisionId
     * @return division in String format
     */
    public static String getById(int divisionId) {
        String division = "";
        JDBC jdbc = new JDBC();
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID=?";
        try(PreparedStatement ps = jdbc.getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, divisionId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                division = rs.getString("Division");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return division;
    }
}
