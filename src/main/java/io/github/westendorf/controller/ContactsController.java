package io.github.westendorf.controller;

import io.github.westendorf.dao.ContactsDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.Contact;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ContactsController extends Controller {
    @FXML
    private TopMenu topMenu;
    @FXML
    private TableView contactsTable;
    @FXML
    private TableColumn tcEdit;
    @FXML
    private TableColumn tcName;
    @FXML
    private TableColumn tcEmail;
    @FXML
    private TableColumn tcDelete;

    private User user;

    /**
     * Required stub from superclass
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Sets user variable and initializes the window
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        initTable();
    }

    /**
     * Fetches and sets the user variable and re-initializes the table
     */
    public void refreshData() {
        this.user = new UsersDao().getById(this.user.getId());
        initTable();
    }

    /**
     * Initalizes the table, including an edit and delete button for each row
     */
    private void initTable() {
        ObservableList<Contact> contacts = FXCollections.observableList(Arrays.asList(this.user.getContacts()));
        contactsTable.setItems(contacts);
        tcEdit.setCellFactory((Callback<TableColumn, TableCell<Contact, String>>) tableColumn -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    Button button = new Button("Edit");
                    button.setOnAction(actionEvent -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/contacts_edit.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(loader.load()));
                            ContactsEditController c = loader.getController();
                            Contact contact = ContactsController.this.user.getContacts()[getIndex()];
                            c.start(user, contact);
                            c.setContactsController(ContactsController.this);
                            stage.show();
                        } catch(IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException();
                        }
                    });
                    setGraphic(button);
                }
            }
        });
        tcName.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>) cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getName()));
        tcEmail.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Contact, String>, ObservableValue<String>>) cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getEmail()));
        tcDelete.setCellFactory((Callback<TableColumn, TableCell>) tableColumn -> new TableCell() {
            @Override
            protected void updateItem(Object o, boolean empty) {
                super.updateItem(o, empty);
                if(!empty) {
                    Button button = new Button("Delete");
                    button.setOnAction(actionEvent -> {
                        Contact contact = ContactsController.this.user.getContacts()[getIndex()];
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Are you sure you want to delete this contact?",
                                ButtonType.OK, ButtonType.CLOSE);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Contact");
                        alert.showAndWait().ifPresent(b -> {
                            if(b == ButtonType.CLOSE) alert.close();
                            else if (b == ButtonType.OK) {
                                new ContactsDao().delete(contact);
                                Alert confAlert = new Alert(Alert.AlertType.INFORMATION,
                                        "You have deleted the contact", ButtonType.OK);
                                confAlert.setTitle("Deleted");
                                confAlert.setHeaderText("Deleted Contact");
                                confAlert.show();
                                refreshData();
                            }
                        });
                    });
                    setGraphic(button);
                }
            }
        });
    }

    /**
     *
     * @return TopMenu
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }

    /**
     * Event handler for add contact button. Opens a new window.
     *
     * @param actionEvent
     */
    public void addContact(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/contacts_edit.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ContactsEditController controller = loader.getController();
            controller.start(this.user);
            controller.setContactsController(this);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
