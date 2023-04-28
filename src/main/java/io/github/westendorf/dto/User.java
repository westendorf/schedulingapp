package io.github.westendorf.dto;

import java.util.ArrayList;
import java.util.List;

public class User extends DataTransferObject {
    private String username = "";
    private List<Appointment> appointments = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private Appointment appointmentCursor = null;
    private Contact contactCursor = null;
    private Customer customerCursor = null;

    public User(int id) {
        super(id);
    }

    public Appointment[] getAppointments() {
        return this.appointments.toArray(new Appointment[this.appointments.size()]);
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public boolean updateAppointment(Appointment appointment) {
        return false;
    }

    public boolean deleteAppointment(Appointment appointment) {
        return false;
    }

    public Contact[] getContacts() {
        return this.contacts.toArray(new Contact[this.contacts.size()]);
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public boolean updateContact(Contact contact) {
        return false;
    }

    public boolean deleteContact(Contact contact) {
        return false;
    }

    public Customer[] getCustomers() {
        return customers.toArray(new Customer[this.customers.size()]);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return false;
    }

    public boolean deleteCustomer(Customer customer) {
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Appointment getAppointmentCursor() {
        return appointmentCursor;
    }

    public void setAppointmentCursor(Appointment appointmentCursor) {
        this.appointmentCursor = appointmentCursor;
    }

    public Contact getContactCursor() {
        return contactCursor;
    }

    public void setContactCursor(Contact contactCursor) {
        this.contactCursor = contactCursor;
    }

    public Customer getCustomerCursor() {
        return customerCursor;
    }

    public void setCustomerCursor(Customer customerCursor) {
        this.customerCursor = customerCursor;
    }

}
