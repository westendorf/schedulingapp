package io.github.westendorf.dao;

import io.github.westendorf.dto.Appointment;
import io.github.westendorf.dto.Contact;
import io.github.westendorf.dto.Customer;
import io.github.westendorf.dto.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsDao extends Dao<Appointment> {

    /**
     * Adds an appointment to the database
     *
     * @param appointment
     * @return boolean Success
     */
    @Override
    public boolean add(Appointment appointment) {
        boolean added = false;
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, 'app', 'app', ?, ?, ?);";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, appointment.getStart());
            ps.setTimestamp(6, appointment.getEnd());
            ps.setInt(7, appointment.getCustomer().getId());
            ps.setInt(8, appointment.getUser().getId());
            ps.setInt(9, appointment.getContact().getId());
            added = ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return added;
    }

    /**
     *
     * Updates an appointment in the database
     *
     * @param appointment
     * @return boolean Success
     */
    @Override
    public boolean update(Appointment appointment) {
        boolean success = false;

        String sql = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Updated_By='app', Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, appointment.getStart());
            ps.setTimestamp(6, appointment.getEnd());
            ps.setInt(7, appointment.getCustomer().getId());
            ps.setInt(8, appointment.getUser().getId());
            ps.setInt(9, appointment.getContact().getId());
            ps.setInt(10, appointment.getId());
            int rows = ps.executeUpdate();
            success = rows == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return success;
    }

    /**
     * Removes the appointment from the database by the id in the appointment object
     *
     * @param appointment
     * @return Boolean success
     */
    @Override
    public boolean delete(Appointment appointment) {
        boolean success = false;
        String sql = "DELETE FROM appointments WHERE Appointment_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, appointment.getId());
            int rows = ps.executeUpdate();
            success = rows == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return success;
    }

    /**
     * Fetches an appointment from the database based on the supplied id
     *
     * @param id
     * @return Appointment
     */
    @Override
    public Appointment getById(int id) {
        Appointment appointment = new Appointment(0, new User(0), new Customer(0, 1), new Contact(0));
        String sql = "SELECT * FROM appointments;";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Customer customer = new CustomersDao().getById(rs.getInt("Contact_ID"));
                Contact contact = new ContactsDao().getById(rs.getInt("Customer_ID"));
                User user = new UsersDao().getById(rs.getInt("User_ID"));
                int appntId = rs.getInt("Appointment_ID");
                Appointment appnt = new Appointment(appntId, user, customer, contact);
                appnt.setTitle(rs.getString("Title"));
                appnt.setDescription(rs.getString("Description"));
                appnt.setLocation(rs.getString("Location"));
                appnt.setType(rs.getString("Type"));
                appnt.setStart(rs.getTimestamp("Start"));
                appnt.setEnd(rs.getTimestamp("End"));
                appointment = appnt;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        return appointment;
    }

    /**
     * Returns All Appointment records in the database
     *
     * @return List of Appointment objects
     */
    @Override
    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments;";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Customer customer = new CustomersDao().getById(rs.getInt("Contact_ID"));
                Contact contact = new ContactsDao().getById(rs.getInt("Customer_ID"));
                User user = new UsersDao().getById(rs.getInt("User_ID"));
                int appntId = rs.getInt("Appointment_ID");
                Appointment appnt = new Appointment(appntId, user, customer, contact);
                appnt.setTitle(rs.getString("Title"));
                appnt.setDescription(rs.getString("Description"));
                appnt.setLocation(rs.getString("Location"));
                appnt.setType(rs.getString("Type"));
                appnt.setStart(rs.getTimestamp("Start"));
                appnt.setEnd(rs.getTimestamp("End"));
                appointments.add(appnt);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        return appointments;
    }

}
