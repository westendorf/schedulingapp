package io.github.westendorf.controller;

import io.github.westendorf.dao.CustomersDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.Customer;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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

public class CustomersController extends Controller {

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn tcEdit;
    @FXML
    private TableColumn<Customer, String> tcId;
    @FXML
    private TableColumn<Customer, String> tcName;
    @FXML
    private TableColumn<Customer, String> tcAddress;
    @FXML
    private TableColumn<Customer, String> tcDivision;
    @FXML
    private TableColumn<Customer, String> tcCountry;
    @FXML
    private TableColumn<Customer, String> tcPostalCode;
    @FXML
    private TableColumn<Customer, String> tcPhoneNumber;
    @FXML
    private TableColumn tcDelete;
    @FXML
    private TopMenu topMenu;

    /**
     * Empty stub required by superclass
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Sets the user variable and initializes the window
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
     * Initializes the customers table with update and delete buttons for each row
     */
    public void initTable() {
        customersTable.setItems(FXCollections.observableList(Arrays.asList(this.user.getCustomers())));
        tcEdit.setCellFactory(tableColumn -> new TableCell() {
            @Override
            protected void updateItem(Object o, boolean empty) {
                super.updateItem(o, empty);
                if(!empty) {
                    Button button = new Button("Edit");
                    button.setOnAction(actionEvent -> {
                        try {
                            Customer customer = CustomersController.this.user.getCustomers()[getIndex()];
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customers_edit.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("Edit Customer");
                            stage.setScene(new Scene(loader.load()));
                            CustomersEditController c = loader.getController();
                            c.start(CustomersController.this.user, customer);
                            c.setCustomersController(CustomersController.this);
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
        tcId.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getId())));
        tcName.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getName()));
        tcAddress.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getAddress()));
        tcDivision.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getDivision()));
        tcPhoneNumber.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getPhoneNumber()));
        tcCountry.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getCountry()));
        tcPostalCode.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getPostalCode()));
        tcDelete.setCellFactory(tableColumn -> new TableCell() {
            @Override
            protected void updateItem(Object o, boolean empty) {
                super.updateItem(o, empty);
                if(!empty) {
                    Button button = new Button("Delete");
                    button.setOnAction(actionEvent -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Are you sure you want to delete this customer?",
                                ButtonType.OK, ButtonType.CLOSE);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Customer");
                        alert.showAndWait().ifPresent(b -> {
                            if(b == ButtonType.CLOSE) alert.close();
                            else if(b == ButtonType.OK) {
                                Customer customer = CustomersController.this.user.getCustomers()[getIndex()];
                                new CustomersDao().delete(customer);
                                Alert confAlert = new Alert(Alert.AlertType.INFORMATION,
                                        "You have deleted the customer",
                                        ButtonType.OK);
                                confAlert.setTitle("Deleted");
                                confAlert.setHeaderText("Deleted customer");
                                confAlert.show();
                                CustomersController.this.user = new UsersDao().getById(CustomersController.this.user.getId());
                                CustomersController.this.initTable();
                            }
                        });
                    });
                    setGraphic(button);
                }
            }
        });
    }

    /**
     * Required to allow setting of user variable in the menu
     *
     * @return TopMenu
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }

    /**
     * Event handler that opens a new window to allow a customer to be added
     *
     * @param actionEvent
     */
    public void addCustomer(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customers_edit.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(loader.load()));
            CustomersEditController c = loader.getController();
            c.setCustomersController(this);
            c.start(this.user);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Sets the user variable
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
