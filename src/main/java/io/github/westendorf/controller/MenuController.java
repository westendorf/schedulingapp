package io.github.westendorf.controller;

import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuController extends Controller {

    @FXML
    private MenuItem customerAppointmentsMenuItem;
    @FXML
    private MenuItem contactSchedulesMenuItem;
    @FXML
    private MenuItem totalsMenuItem;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menu;
    @FXML
    private MenuItem dashboardMenuItem;
    @FXML
    private MenuItem appointmentsMenuItem;
    @FXML
    private MenuItem contactsMenuItem;
    @FXML
    private MenuItem customersMenuItem;

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Opens a window for the dashboard and greys out its label in the top menu
     *
     * @param actionEvent
     */
    public void goToDashboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard");
            stage.show();
            DashboardController dc = loader.getController();
            dc.getTopMenu().getController().start(this.user);
            dc.start(this.user);
            dc.getTopMenu().getController().getDashboardMenuItem().setDisable(true);
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Opens a window for the appointments window and greys out its label in the top menu
     *
     * @param actionEvent
     */
    public void goToAppointments(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/appointments.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Appointments");
            stage.show();
            AppointmentsController ac = loader.getController();
            ac.getTopMenu().getController().start(this.user);
            ac.start(this.user);
            ac.getTopMenu().getController().getAppointmentsMenuItem().setDisable(true);
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Opens a window for the contacts window and greys out its label in the top menu
     *
     * @param actionEvent
     */
    public void goToContacts(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/contacts.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Contacts");
            stage.show();
            ContactsController cc = loader.getController();
            cc.getTopMenu().getController().start(this.user);
            cc.start(this.user);
            cc.getTopMenu().getController().getContactsMenuItem().setDisable(true);
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Opens a window for the customers window and greys out its label in the top menu
     *
     * @param actionEvent
     */
    public void goToCustomers(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customers.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Customers");
            stage.show();
            CustomersController cc = loader.getController();
            cc.getTopMenu().getController().start(this.user);
            cc.start(this.user);
            cc.getTopMenu().getController().getCustomersMenuItem().setDisable(true);
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Getter for appointments menu item
     *
     * @return
     */
    public MenuItem getAppointmentsMenuItem() {
        return appointmentsMenuItem;
    }

    /**
     * Getter for dashboard menu item
     *
     * @return
     */
    public MenuItem getDashboardMenuItem() {
        return dashboardMenuItem;
    }

    /**
     * Getter for contacts menu item
     *
     * @return
     */
    public MenuItem getContactsMenuItem() {
        return contactsMenuItem;
    }

    /**
     * Getter for customers menu item
     *
     * @return
     */
    public MenuItem getCustomersMenuItem() {
        return customersMenuItem;
    }

    /**
     * Getter for user
     *
     * @return
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Empty stub for required superclass method
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {

    }

    /**
     * Empty stub for required superclass method
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Getter for customer appointments report window menu item
     *
     * @return
     */
    public void goToCustomerAppointmentsReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer_appointments_report.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            CustomerAppointmentsReportController c = loader.getController();
            c.getTopMenu().getController().start(this.user);
            c.start(this.user);
            c.getTopMenu().getController().customerAppointmentsMenuItem.setDisable(true);
            stage.show();
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Getter for contact schedules report window menu item
     *
     * @return
     */
    public void goToContactSchedulesReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/contact_schedules_report.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ContactSchedulesReportController c = loader.getController();
            c.getTopMenu().getController().start(this.user);
            c.start(this.user);
            c.getTopMenu().getController().contactSchedulesMenuItem.setDisable(true);
            stage.show();
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Getter for total report window menu item
     *
     * @return
     */
    public void goToTotalsReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/totals_report.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            TotalsReportController c = loader.getController();
            c.getTopMenu().getController().start(this.user);
            c.start(this.user);
            c.getTopMenu().getController().totalsMenuItem.setDisable(true);
            stage.show();
            menuBar.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
