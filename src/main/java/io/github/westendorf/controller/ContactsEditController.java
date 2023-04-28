package io.github.westendorf.controller;

import io.github.westendorf.dao.ContactsDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.Contact;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactsEditController extends Controller {

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button addUpdateButton;

    private User user;
    private ContactsController contactsController;

    /**
     * Empty stub from superclass
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Sets the user variable and initializes the window based on whether the user wants to add or update a contact
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        if(dto instanceof Contact) {
            this.user.setContactCursor((Contact)dto);
            addUpdateButton.setText("Update");
            idTextField.setText(String.valueOf(this.user.getContactCursor().getId()));
            nameTextField.setText(this.user.getContactCursor().getName());
            emailTextField.setText(this.user.getContactCursor().getEmail());
        } else {
            addUpdateButton.setText("Add");
            idTextField.setText("0");
        }
    }

    /**
     * Event handler for when the add/update button is pressed
     * Returns control back to the Contacts window
     *
     * @param actionEvent
     */
    public void saveContact(ActionEvent actionEvent) {
        if(this.user.getContactCursor() != null) {
            Contact contact = this.user.getContactCursor();
            contact.setName(nameTextField.getText());
            contact.setEmail(emailTextField.getText());
            new ContactsDao().update(contact);
        } else {
            Contact contact = new Contact(0);
            contact.setName(nameTextField.getText());
            contact.setEmail(emailTextField.getText());
            new ContactsDao().add(contact);
        }
        this.user = new UsersDao().getById(this.user.getId());
        this.contactsController.start(user);

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    /**
     * This is necessary to update the results table in the contacts window
     *
     * @param contactsController
     */
    public void setContactsController(ContactsController contactsController) {
        this.contactsController = contactsController;
    }
}
