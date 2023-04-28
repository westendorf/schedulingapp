package io.github.westendorf.dao;

import io.github.westendorf.dto.Customer;
import io.github.westendorf.util.geography.Countries;
import io.github.westendorf.util.geography.Divisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersDao extends Dao<Customer> {

    /**
     * Adds a customer record in database
     *
     * @param customer
     * @return boolean Success
     */
    @Override
    public boolean add(Customer customer) {
        boolean added;
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, 'app', 'app', ?);";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setInt(5, customer.getDivisionId());
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
     * Updates a customer record in the database
     *
     * @param customer
     * @return boolean Success
     */
    @Override
    public boolean update(Customer customer) {
        boolean success = false;
        String sql = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Last_Updated_By='app', Division_ID=? WHERE Customer_ID=?";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setInt(5, customer.getDivisionId());
            ps.setInt(6, customer.getId());
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
     * Deletes a customer record in the database
     * <p>
     * When deleting a customer record, all of the customer’s appointments must be deleted first, due to foreign key constraints.
     *
     * @param customer
     * @return boolean Success
     */
    @Override
    public boolean delete(Customer customer) {
        boolean deletedCust = false;

        String sqlAppnts = "DELETE FROM appointments WHERE Customer_ID=?";
        try(PreparedStatement preparedStatement = getNewConnection().prepareStatement(sqlAppnts)) {
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        String sql = "DELETE FROM customers WHERE Customer_ID=?";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, customer.getId());
            int deletedCustInt = ps.executeUpdate();
            deletedCust = deletedCustInt > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        return deletedCust;
    }

    /**
     * Fetches a customer record from the database and stores in a Customer object
     *
     * @param id
     * @return Customer
     */
    @Override
    public Customer getById(int id) {
        Customer customer = null;
        String sql = "SELECT * FROM customers WHERE Customer_ID=?";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customer = new Customer(rs.getInt("Customer_ID"), rs.getInt("Division_ID"));
                customer.setName(rs.getString("Customer_Name"));
                customer.setAddress(rs.getString("Address"));
                customer.setPostalCode(rs.getString("Postal_Code"));
                customer.setPhoneNumber(rs.getString("Phone"));
                customer.setDivisionId(rs.getInt("Division_ID"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }
        customer.setDivision(Divisions.getById(customer.getDivisionId()));
        customer.setCountry(Countries.getFromDivisionId(customer.getDivisionId()));
        return customer;
    }

    /**
     * Fetches all customers from the database
     *
     * @return List of customers
     */
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers;";
        try (PreparedStatement ps = getNewConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("Customer_ID"), rs.getInt("Division_ID"));
                customer.setName(rs.getString("Customer_Name"));
                customer.setAddress(rs.getString("Address"));
                customer.setPostalCode(rs.getString("Postal_Code"));
                customer.setPhoneNumber(rs.getString("Phone"));
                customer.setDivisionId(rs.getInt("Division_ID"));
                customer.setDivision(Divisions.getById(customer.getDivisionId()));
                customer.setCountry(Countries.getFromDivisionId(customer.getDivisionId()));
                customers.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeConnection();
        }

        return customers;
    }


}
