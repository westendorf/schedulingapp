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

public class UsersDao extends Dao<User> {

    /**
     * Adds a user to the database
     *
     * @param user
     * @return boolean Success
     */
    @Override
    public boolean add(User user) {
        boolean added = false;
        String sql = "INSERT INTO users (User_Name, Created_By, Last_Updated_By) VALUES (?, 'app', 'app');";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
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
     * Updates a user in the database
     *
     * @param user
     * @return boolean Success
     */
    @Override
    public boolean update(User user) {
        boolean updated = false;
        String sql = "UPDATE users SET User_Name=? WHERE User_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setInt(2, user.getId());
            int rows = ps.executeUpdate();
            updated = rows == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return updated;
    }

    /**
     * Deletes a user from the database
     *
     * @param user
     * @return boolean Success
     */
    @Override
    public boolean delete(User user) {
        boolean success = false;
        String sql = "DELETE FROM users WHERE User_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            success = ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return success;
    }

    /**
     * Fetches a user from the database based on the provided id
     *
     * @param id
     * @return User
     */
    @Override
    public User getById(int id) {
        User user = new User(0);
        String sql = "SELECT * FROM users WHERE User_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user = new User(id);
                user.setUsername(rs.getString("User_Name"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        new CustomersDao().getAll().forEach(user::addCustomer);
        new ContactsDao().getAll().forEach(user::addContact);
        User finalUser = user;
        getAllAppointments(user).stream()
                .filter(a -> a.getUser().getId() == finalUser.getId())
                .forEach(user::addAppointment);

        return user;
    }

    /**
     * Fetches all user records from the database
     *
     * @return List of users
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User(rs.getInt("User_ID"));
                user.setUsername(rs.getString("User_Name"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        users.forEach(user -> {
            new ContactsDao().getAll().forEach(user::addContact);
            new CustomersDao().getAll().forEach(user::addCustomer);
            getAllAppointments(user).stream()
                    .filter(a -> a.getUser().getId() == user.getId())
                    .forEach(user::addAppointment);
        });

        return users;
    }

    /**
     * Retrieves all appointments for a particular user
     *
     * @param user
     * @return List of appointments
     */
    private List<Appointment> getAllAppointments(User user) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments;";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Customer customer = new CustomersDao().getById(rs.getInt("Customer_ID"));
                Contact contact = new ContactsDao().getById(rs.getInt("Contact_ID"));
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
