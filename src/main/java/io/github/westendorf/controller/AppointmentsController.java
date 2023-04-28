package io.github.westendorf.controller;

import io.github.westendorf.dao.AppointmentsDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.Appointment;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentsController extends Controller {
    @FXML private TopMenu topMenu;

    @FXML private AnchorPane filterContainer;
    @FXML private DatePicker datePicker;
    @FXML private ToggleGroup radioBtnToggleGroup = new ToggleGroup();
    @FXML private RadioButton inMonthRadioBtn;
    @FXML private RadioButton inWeekRadioBtn;
    @FXML private RadioButton allRadioBtn;
    @FXML private ComboBox<String> customersComboBox;
    @FXML private ComboBox<String> contactsComboBox;

    @FXML private TableView appointmentsTable;
    @FXML private TableColumn<Appointment, String> tcEdit;
    @FXML private TableColumn<Appointment, String> tcId;
    @FXML private TableColumn<Appointment, String> tcTitle;
    @FXML private TableColumn<Appointment, String> tcDescription;
    @FXML private TableColumn<Appointment, String> tcLocation;
    @FXML private TableColumn<Appointment, String> tcType;
    @FXML private TableColumn<Appointment, String> tcUserId;
    @FXML private TableColumn<Appointment, String> tcCustomerId;
    @FXML private TableColumn<Appointment, String> tcCustomerName;
    @FXML private TableColumn<Appointment, String> tcContactName;
    @FXML private TableColumn<Appointment, String> tcStartDate;
    @FXML private TableColumn<Appointment, String> tcStartTime;
    @FXML private TableColumn<Appointment, String> tcEndDate;
    @FXML private TableColumn<Appointment, String> tcEndTime;
    @FXML private TableColumn<Appointment, String> tcDelete;

    /**
     * Automatically called on load from JavaFX
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initRadioButtons();
    }

    /**
     * Custom lifecycle event for passing the User object and other data between windows
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        refreshData();
        initTable(FXCollections.observableList(Arrays.asList(this.user.getAppointments())));
        initCustomerComboBox();
        initContactsComboBox();
        initDatePicker();
    }

    /**
     * This initializes the datepicker by making it today
     */
    private void initDatePicker() {
        this.datePicker.setValue(LocalDate.now());
    }

    /**
     * This initializes the the customer combobox. The default is "All".
     */
    private void initCustomerComboBox() {
        List<String> names = new ArrayList<>();
        names.add("All");
        Arrays.stream(this.user.getCustomers()).forEach(c -> names.add(c.getName()));
        customersComboBox.setItems(FXCollections.observableList(names));
    }

    /**
     * This initializes the contacts combobox. The default is "All".
     */
    private void initContactsComboBox() {
        List<String> names = new ArrayList<>();
        names.add("All");
        Arrays.stream(this.user.getContacts()).forEach(c -> names.add(c.getName()));
        contactsComboBox.setItems(FXCollections.observableList(names));
    }

    /**
     * This updates the user object to a newly fetched user object
     */
    private void refreshData() {
        User user = new UsersDao().getById(this.user.getId());
        this.user = user;
        this.topMenu.getController().setUser(user);
    }

    /**
     * This sets the items in the table and sets up each table column.
     * Edit buttons are in the first column and delete buttons are in the last column.
     *
     * I used lambda functions for each table column with a String value to eliminate the need for a full declaration of a functional interface.
     *
     * @param appointments
     */
    public void initTable(ObservableList<Appointment> appointments) {
        appointmentsTable.setItems(appointments);

        tcEdit.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    Button button = new Button("Edit");
                    button.setOnAction(actionEvent -> {
                        try {
                            Appointment appointment = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/appointments_edit.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("Edit Appointment");
                            Scene scene = new Scene(loader.load());
                            stage.setScene(scene);
                            AppointmentsEditController c = loader.getController();
                            c.start(user, appointment);
                            c.setAppointmentsController(AppointmentsController.this);
                            stage.show();
                        } catch (IOException e) {
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
        tcTitle.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getTitle()));
        tcDescription.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getDescription()));
        tcLocation.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getLocation()));
        tcType.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getType()));
        tcUserId.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getUser().getId())));
        tcCustomerId.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getCustomer().getId())));
        tcCustomerName.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getCustomer().getName()));
        tcContactName.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getContact().getName()));
        tcStartDate.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy"))));
        tcStartTime.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("h:mm a"))));
        tcEndDate.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy"))));
        tcEndTime.setCellValueFactory(cellDataFeatures
                -> new SimpleStringProperty(cellDataFeatures.getValue().getEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("h:mm a"))));
        tcDelete.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    Button button = new Button("Delete");
                    button.setOnAction(actionEvent -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Are you sure you want to delete this appointment?",
                                ButtonType.OK, ButtonType.CLOSE);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Appointment");
                        alert.showAndWait().ifPresent(b -> {
                            if(b == ButtonType.CLOSE) alert.close();
                            if(b == ButtonType.OK) {
                                new AppointmentsDao().delete(appointment);
                                Alert alertDeleted = new Alert(Alert.AlertType.INFORMATION, "You have deleted the appointment",
                                        ButtonType.CLOSE);
                                alertDeleted.setTitle("Deleted");
                                alertDeleted.setHeaderText("Deleted Appointment");
                                alertDeleted.show();

                                user = new UsersDao().getById(user.getId());
                                initTable(FXCollections.observableList(Arrays.asList(user.getAppointments())));
                            }
                        });
                    });
                    setGraphic(button);
                }
            }
        });
    }

    /**
     * Creates and utilizes a toggle group
     */
    public void initRadioButtons() {
        inMonthRadioBtn.setToggleGroup(radioBtnToggleGroup);
        inWeekRadioBtn.setToggleGroup(radioBtnToggleGroup);
        allRadioBtn.setToggleGroup(radioBtnToggleGroup);
        allRadioBtn.selectedProperty().setValue(true);
    }

    /**
     *
     * @return TopMenu
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }

    /**
     * This is called when any control is changed. All controls are taken into account. The results in the table are updated.
     * @param actionEvent
     */
    public void filter(ActionEvent actionEvent) {
        List<Appointment> filteredResults = Arrays.asList(this.user.getAppointments());
        Timestamp selectedTime = Timestamp.valueOf(ZonedDateTime.of(datePicker.getValue(), LocalTime.MIDNIGHT, ZoneId.systemDefault()).toLocalDateTime());
        if(inMonthRadioBtn.isSelected()) filteredResults = filterByMonth(filteredResults, selectedTime);
        else if(inWeekRadioBtn.isSelected()) filteredResults = filterByWeek(filteredResults, selectedTime);
        if(customersComboBox.getValue() != null) filteredResults = filterByCustomer(filteredResults, customersComboBox.getValue());
        if(contactsComboBox.getValue() != null) filteredResults = filterByContact(filteredResults, contactsComboBox.getValue());
        initTable(FXCollections.observableList(filteredResults));
    }

    /**
     * This filters a list of appointments whose start date is after a given time and before that time plus 1 month
     *
     * @param appointments
     * @param time
     * @return List of filtered Appointments
     */
    private List<Appointment> filterByMonth(List<Appointment> appointments, Timestamp time) {
        return appointments.stream()
                .filter(a -> {
                    Timestamp timeEnd = Timestamp.valueOf(time.toLocalDateTime().plusMonths(1));
                    return a.getStart().after(time) && a.getStart().before(timeEnd);
                })
                .collect(Collectors.toList());
    }

    /**
     * This filters a list of appointments by ensuring each appointment has a start date that is between a certain date and a week from that date
     *
     * @param appointments
     * @param time
     * @return List of filtered Appointments
     */
    private List<Appointment> filterByWeek(List<Appointment> appointments, Timestamp time) {
        return appointments.stream()
                .filter(a -> {
                    Timestamp timeEnd = Timestamp.valueOf(time.toLocalDateTime().plusWeeks(1));
                    return a.getStart().after(time) && a.getStart().before(timeEnd);
                })
                .collect(Collectors.toList());
    }

    /**
     * Only appointments with a certain customer name are returned
     *
     * @param appointments
     * @param customer
     * @return List of filtered Appointments
     */
    private List<Appointment> filterByCustomer(List<Appointment> appointments, String customer) {
        return customer.equals("All")
        ? appointments
        : appointments.stream()
                .filter(a -> a.getCustomer().getName().equals(customer))
                .collect(Collectors.toList());
    }

    /**
     * This method filters a list of Appointments, returning only those appointments whose contact name is equal to the provided contact name
     * @param appointments
     * @param contact
     * @return List of filtered Appointments
     */
    private List<Appointment> filterByContact(List<Appointment> appointments, String contact) {
        return contact.equals("All")
        ? appointments
        : appointments.stream()
                .filter(a -> a.getContact().getName().equals(contact))
                .collect(Collectors.toList());
    }

    /**
     * This method is called when the user wants to add an appointment
     *
     * @param actionEvent
     */
    public void addAppointment(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/appointments_edit.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Appointment");
            stage.setScene(new Scene(loader.load()));
            stage.show();
            AppointmentsEditController controller = loader.getController();
            controller.start(this.user);
            controller.setAppointmentsController(this);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
