package io.github.westendorf.controller;

import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TotalsReportController extends Controller {

    @FXML
    private Label totalCustomers;
    @FXML
    private Label totalAppointments;
    @FXML
    private Label totalContacts;
    @FXML
    private TopMenu topMenu;

    /**
     * Sets the user variable and initializes the window
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        totalCustomers.setText(String.valueOf(this.user.getCustomers().length));
        totalAppointments.setText(String.valueOf(this.user.getAppointments().length));
        totalContacts.setText(String.valueOf(this.user.getContacts().length));
    }

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
     * Getter for top menu
     *
     * @return
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }
}
