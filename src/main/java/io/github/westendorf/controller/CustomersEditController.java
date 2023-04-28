package io.github.westendorf.controller;

import io.github.westendorf.dao.CustomersDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.Customer;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.util.geography.Countries;
import io.github.westendorf.util.geography.Divisions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersEditController extends Controller {

    @FXML
    public Label fieldsRequiredLabel;
    @FXML
    private TextField idTextField;
    @FXML
    private ComboBox<String> countriesCombo;
    @FXML
    private ComboBox<String> divisionCombo;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private Button addUpdateButton;

    private User user;
    private CustomersController customersController;

    /**
     * Initializes the window
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countriesCombo.setItems(FXCollections.observableList(Countries.getAll()));
    }

    /**
     * Initializes the window based on whether the user wants to add or update a customer
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        if (dto instanceof Customer) {
            Customer customer = (Customer) dto;
            this.user.setCustomerCursor(customer);
            addUpdateButton.setText("Update");
            autofillForm();
        } else {
            addUpdateButton.setText("Add");
            idTextField.setText("0");
        }
    }

    /**
     * Fills out the form with the DTO provided
     */
    private void autofillForm() {
        Customer customer = this.user.getCustomerCursor();
        idTextField.setText(String.valueOf(customer.getId()));
        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        countriesCombo.setValue(customer.getCountry());
        updateDivisionsComboBox(null);
        divisionCombo.setValue(customer.getDivision());
        postalCodeTextField.setText(customer.getPostalCode());
        phoneTextField.setText(customer.getPhoneNumber());
    }

    /**
     * Indicates whether or not the form is valid
     *
     * @return boolean valid
     */
    private boolean formValidates() {
        return !nameTextField.getText().isEmpty()
                && !addressTextField.getText().isEmpty()
                && !postalCodeTextField.getText().isEmpty()
                && !phoneTextField.getText().isEmpty()
                && !countriesCombo.getValue().isEmpty()
                && !divisionCombo.getValue().isEmpty();
    }

    /**
     * Event handler that either adds or updates a customer
     *
     * @param actionEvent
     */
    public void saveCustomer(ActionEvent actionEvent) {
        if(formValidates()) {
            if (this.user.getCustomerCursor() != null) {
                Customer customer = loadCustomerWithInputs(this.user.getCustomerCursor());
                new CustomersDao().update(customer);
            } else {
                int divisionId = Divisions.getId(countriesCombo.getValue(), divisionCombo.getValue());
                Customer customer = loadCustomerWithInputs(new Customer(0, divisionId));
                new CustomersDao().add(customer);
            }
            getCustomersController().setUser(new UsersDao().getById(this.user.getId()));
            getCustomersController().initTable();
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        } else {
            fieldsRequiredLabel.setVisible(true);
        }
    }

    /**
     * Required to update the previous windows results table before regaining control
     *
     * @return
     */
    public CustomersController getCustomersController() {
        return this.customersController;
    }

    /**
     * Required to update the previous windows results table before regaining control
     *
     * @param customersController
     */
    public void setCustomersController(CustomersController customersController) {
        this.customersController = customersController;
    }

    /**
     * Edits a customer object with the controls in the form
     *
     * @param customer
     * @return
     */
    private Customer loadCustomerWithInputs(Customer customer) {
        customer.setName(nameTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setDivisionId(Divisions.getId(countriesCombo.getValue(), divisionCombo.getValue()));
        customer.setPostalCode(postalCodeTextField.getText());
        customer.setPhoneNumber(phoneTextField.getText());
        return customer;
    }

    /**
     * Required to keep countries and divisions comboboxes synced up
     *
     * @param actionEvent
     */
    public void updateDivisionsComboBox(ActionEvent actionEvent) {
        String country = countriesCombo.getValue();
        divisionCombo.setItems(FXCollections.observableList(Divisions.getAllByCountry(country)));
    }
}
