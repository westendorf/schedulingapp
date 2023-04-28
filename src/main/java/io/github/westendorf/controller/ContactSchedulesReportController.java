package io.github.westendorf.controller;

import io.github.westendorf.dto.Appointment;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ContactSchedulesReportController extends Controller {
    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private TableColumn<Appointment, String> tcContact;
    @FXML
    private TableColumn<Appointment, String> tcCustomerId;
    @FXML
    private TableColumn<Appointment, String> tcAppointmentId;
    @FXML
    private TableColumn<Appointment, String> tcTitle;
    @FXML
    private TableColumn<Appointment, String> tcType;
    @FXML
    private TableColumn<Appointment, String> tcDescription;
    @FXML
    private TableColumn<Appointment, String> tcStart;
    @FXML
    private TableColumn<Appointment, String> tcEnd;

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
        initTable();
    }

    /**
     * Initializes the table: shows appointments for each contact in the database
     */
    private void initTable() {
        ObservableList<Appointment> appointments = FXCollections.observableList(Arrays.asList(this.user.getAppointments()));
        scheduleTable.setItems(appointments);
        tcContact.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getContact().getName()));
        tcCustomerId.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getCustomer().getId())));
        tcAppointmentId.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getId())));
        tcTitle.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getTitle()));
        tcType.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getType()));
        tcDescription.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getDescription()));
        tcStart.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy h:m a"))));
        tcEnd.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy h:m a"))));
    }

    /**
     * Initialization stub required from superclass
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Getter for top menu
     * @return
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }
}
