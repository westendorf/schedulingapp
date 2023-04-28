package io.github.westendorf.dto;

import java.sql.Timestamp;

public class Appointment extends DataTransferObject {
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Customer customer;
    private User user;
    private Contact contact;

    public Appointment(int id, User user, Customer customer, Contact contact) {
        super(id);
        this.user = user;
        this.customer = customer;
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user == null ? new User(0) : user;
    }

    public Customer getCustomer() {
        return customer == null ? new Customer(0, 0) : customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Contact getContact() {
        return contact == null ? new Contact(0) : contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public boolean hasConflict(Appointment appointment) {
        return (this.getStart().before(appointment.getStart()) && this.getEnd().after(appointment.getStart()))
                || (this.getStart().equals(appointment.getStart()))
                || (this.getStart().before(appointment.getEnd()) && this.getEnd().after(appointment.getEnd()));
    }

}
