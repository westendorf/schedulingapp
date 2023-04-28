package io.github.westendorf.dao;

import io.github.westendorf.dto.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactsDao extends Dao<Contact> {

    /**
     * Adds a contact to the database
     *
     * @param contact
     * @return boolean Success
     */
    @Override
    public boolean add(Contact contact) {
        boolean added = false;
        String sql = "INSERT INTO contacts (Contact_Name, Email) VALUES (?, ?)";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            int rows = ps.executeUpdate();
            added = rows == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return added;
    }

    /**
     * Updates a contact record in the database
     *
     * @param contact
     * @return boolean Success
     */
    @Override
    public boolean update(Contact contact) {
        boolean updated = false;
        String sql = "UPDATE contacts SET Contact_Name=?, Email=? WHERE Contact_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            ps.setInt(3, contact.getId());
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
     * Deletes a contact record from the database
     *
     * @param contact
     * @return boolean Success
     */
    @Override
    public boolean delete(Contact contact) {
        boolean deleted = false;
        String sql = "DELETE FROM contacts WHERE Contact_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, contact.getId());
            int rows = ps.executeUpdate();
            deleted = rows == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return deleted;
    }

    /**
     * Fetches a contact from the database based on the provided id
     *
     * @param id
     * @return Contact
     */
    @Override
    public Contact getById(int id) {
        Contact contact = null;
        String sql = "SELECT * FROM contacts WHERE Contact_ID=?";
        try(PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                contact = new Contact(rs.getInt("Contact_ID"));
                contact.setName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return contact;
    }

    /**
     * Fetches all contacts from the database
     *
     * @return List of contacts
     */
    @Override
    public List<Contact> getAll() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Contact contact = new Contact(rs.getInt("Contact_ID"));
                contact.setName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
                contacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        return contacts;
    }


}
