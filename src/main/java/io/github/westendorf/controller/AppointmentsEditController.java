package io.github.westendorf.controller;

import io.github.westendorf.dao.AppointmentsDao;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentsEditController extends Controller {

    @FXML
    private Button addUpdateButton;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startHourTextField;
    @FXML
    private TextField startMinTextField;
    @FXML
    private ComboBox<String> startMeridiemCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endHourTextField;
    @FXML
    private TextField endMinTextField;
    @FXML
    private ComboBox<String> endMeridiemCombo;
    @FXML
    private ComboBox<String> contactCombo;
    @FXML
    private ComboBox<String> customerCombo;
    @FXML
    private Label fieldsRequiredLabel;

    private User user;
    private AppointmentsController appointmentsController;

    /**
     * Empty initialization block required by superclass
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Sets user, text for add/update button, sets appointment DTO if applicable, and initializes controls
     *
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        addUpdateButton.setText("Add");
        idTextField.setText("0");
        if(dto instanceof Appointment) {
            this.user.setAppointmentCursor((Appointment)dto);
            addUpdateButton.setText("Update");
            autopopulateForm();
        }
        initMeridiemComboBoxes();
        initContactsComboBox();
        initCustomersComboBox();
    }

    /**
     * It is required to put a reference to the appointments controller (previous window) so that the table there can be updated when the user is done adding or updating a specific appointment
     *
     * @param appointmentsController
     */
    public void setAppointmentsController(AppointmentsController appointmentsController) {
        this.appointmentsController = appointmentsController;
    }

    /**
     * This method initializes the contacts combobox
     */
    private void initContactsComboBox() {
        ObservableList<String> contactNames = FXCollections
                .observableList(Arrays.stream(this.user.getContacts())
                .map(Contact::getName)
                .collect(Collectors.toList()));
        contactCombo.setItems(contactNames);
    }

    /**
     * This method initializes the customers combobox
     */
    private void initCustomersComboBox() {
        ObservableList<String> customerNames = FXCollections
                .observableList(Arrays.stream(this.user.getCustomers())
                .map(Customer::getName)
                .collect(Collectors.toList()));
        customerCombo.setItems(customerNames);
    }

    /**
     * This method sets up the AM/PM comboboxes
     */
    private void initMeridiemComboBoxes() {
        ObservableList<String> amPm = FXCollections.observableList(Arrays.asList("AM", "PM"));
        startMeridiemCombo.setItems(amPm);
        endMeridiemCombo.setItems(amPm);

        if(this.user.getAppointmentCursor() == null) {
            startMeridiemCombo.setValue("AM");
            endMeridiemCombo.setValue("AM");
        }

    }

    /**
     * This is only called if the user is editing an existing appointment
     */
    private void autopopulateForm() {
        Appointment a = this.user.getAppointmentCursor();
        idTextField.setText(String.valueOf(a.getId()));
        titleTextField.setText(a.getTitle());
        descriptionTextField.setText(a.getDescription());
        locationTextField.setText(a.getLocation());
        typeTextField.setText(a.getType());

        LocalDateTime startDate = a.getStart().toLocalDateTime();
        startDatePicker.setValue(startDate.toLocalDate());
        startHourTextField.setText(startDate.format(DateTimeFormatter.ofPattern("h")));
        startMinTextField.setText(startDate.format(DateTimeFormatter.ofPattern("mm")));
        startMeridiemCombo.setValue(startDate.format(DateTimeFormatter.ofPattern("a")));

        LocalDateTime endDate = a.getEnd().toLocalDateTime();
        endDatePicker.setValue(endDate.toLocalDate());
        endHourTextField.setText(endDate.format(DateTimeFormatter.ofPattern("h")));
        endMinTextField.setText(endDate.format(DateTimeFormatter.ofPattern("mm")));
        endMeridiemCombo.setValue(endDate.format(DateTimeFormatter.ofPattern("a")));

        contactCombo.setValue(a.getContact().getName());
        customerCombo.setValue(a.getCustomer().getName());
    }

    /**
     * This method validates the form upon submission based on the no out of business hours schedules (in EST), and no empty fields
     *
     * @return
     * @throws NumberFormatException
     */
    private boolean validateForm() throws NumberFormatException {
        int startHour = Integer.parseInt(startHourTextField.getText());
        int startMin = Integer.parseInt(startMinTextField.getText());
        int endHour = Integer.parseInt(endHourTextField.getText());
        int endMin = Integer.parseInt(endMinTextField.getText());
        String amPmStart = startMeridiemCombo.getValue();
        String amPmEnd = endMeridiemCombo.getValue();
        int startHour24 = twelveToTwentyFourHour(startHour, amPmStart);
        int endHour24 = twelveToTwentyFourHour(endHour, amPmEnd);

        ZonedDateTime workDayStart = ZonedDateTime.of(LocalDate.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonthValue(), startDatePicker.getValue().getDayOfMonth()),
                LocalTime.of(8, 0), ZoneId.of("US/Eastern"));
        ZonedDateTime workDayEnd = ZonedDateTime.of(LocalDate.of(endDatePicker.getValue().getYear(), endDatePicker.getValue().getMonthValue(), endDatePicker.getValue().getDayOfMonth()),
                LocalTime.of(22, 0), ZoneId.of("US/Eastern"));
        ZonedDateTime appointmentStart = ZonedDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonthValue(), startDatePicker.getValue().getDayOfMonth(), startHour24, startMin, 0, 0, ZoneId.systemDefault());
        ZonedDateTime appointmentEnd = ZonedDateTime.of(endDatePicker.getValue().getYear(), endDatePicker.getValue().getMonthValue(), endDatePicker.getValue().getDayOfMonth(), endHour24, endMin, 0, 0, ZoneId.systemDefault());
        workDayStart = workDayStart.minusMinutes(1);
        workDayEnd = workDayEnd.plusMinutes(1);
        boolean appointmentStartAfterOpening = workDayStart.isBefore(appointmentStart);
        boolean appointmentEndBeforeClosing = workDayEnd.isAfter(appointmentEnd);
        if(!appointmentEndBeforeClosing || !appointmentStartAfterOpening) {
            Alert outOfBusinessHoursAlert = new Alert(Alert.AlertType.ERROR, "Appointment is out of business hours.", ButtonType.OK);
            outOfBusinessHoursAlert.setTitle("Error");
            outOfBusinessHoursAlert.setHeaderText("Business hours are from 8AM to 10PM EST all week");
            outOfBusinessHoursAlert.show();
        }

        return !titleTextField.getText().isEmpty()
                && !descriptionTextField.getText().isEmpty()
                && !locationTextField.getText().isEmpty()
                && !typeTextField.getText().isEmpty()
                && startDatePicker.getValue() != null
                && endDatePicker.getValue() != null
                && startHour >= 1 && startHour <= 12
                && startMin >= 0 && startMin < 60
                && appointmentStartAfterOpening
                && appointmentEndBeforeClosing
                && endHour >= 1 && endHour <= 12
                && endMin >= 0 && endMin < 60
                && contactCombo.getValue() != null
                && customerCombo.getValue() != null;
    }

    public int twelveToTwentyFourHour(int hour, String amPm) {
        int hour24 = (amPm.equals("PM") && hour != 12) || (hour == 12 && amPm.equals("AM")) ? hour + 12 : hour;
        if(hour24 == 24) hour24 = 0;
        return hour24;
    }

    /**
     * This checks if the form is valid and that there are no double-booked appointments
     * Saves the appointment in the form if everything checks out
     *
     * @param actionEvent
     */
    public void onClickSaveButton(ActionEvent actionEvent) {
        boolean formValid;
        try {
            formValid = validateForm();
        } catch(NumberFormatException e) {
            formValid = false;
        }
        if(formValid) {
            int customerIndex = customerCombo.getSelectionModel().getSelectedIndex();
            String customerName = customerCombo.getValue();
            Customer customer = this.user.getCustomers()[customerIndex];
            if(!customerName.equals(customer.getName())) throw new RuntimeException("customer index should match customer name");

            int contactIndex = contactCombo.getSelectionModel().getSelectedIndex();
            String contactName = contactCombo.getValue();
            Contact contact = this.user.getContacts()[contactIndex];
            if(!contact.getName().equals(contactName)) throw new RuntimeException("contact index should match contact name");

            Appointment appointment = user.getAppointmentCursor() == null
                    ? new Appointment(0, this.user, customer, contact)
                    : new Appointment(user.getAppointmentCursor().getId(), this.user, customer, contact);
            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());

            LocalDate startDate = startDatePicker.getValue();
            int startMin = Integer.parseInt(startMinTextField.getText());
            int startHour = twelveToTwentyFourHour(Integer.parseInt(startHourTextField.getText()), startMeridiemCombo.getValue());
            appointment.setStart(Timestamp.valueOf(ZonedDateTime.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), startHour, startMin, 0, 0, ZoneId.systemDefault()).toLocalDateTime()));

            LocalDate endDate = endDatePicker.getValue();
            int endMin = Integer.parseInt(endMinTextField.getText());
            int endHour = twelveToTwentyFourHour(Integer.parseInt(endHourTextField.getText()), endMeridiemCombo.getValue());
            appointment.setEnd(Timestamp.valueOf(ZonedDateTime.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(), endHour, endMin, 0, 0, ZoneId.systemDefault()).toLocalDateTime()));

            boolean hasConflict = false;
            List<Appointment> appointments = new ArrayList<>(Arrays.asList(this.user.getAppointments()));
            appointments.add(appointment);
            List<ArrayList<Integer>> appointmentRegistry = new ArrayList<>();
            for(int i = 0; i < appointments.size(); i++) {
                iteratingLoop:
                for(int j = 0; j < appointments.size(); j++) {
                    if(i == j) continue;
                    for(ArrayList<Integer> a : appointmentRegistry) {
                        if(a.contains(i) && a.contains(j)) continue iteratingLoop;
                    }
                    appointmentRegistry.add(new ArrayList<>(Arrays.asList(i, j)));
                    if(appointments.get(i).hasConflict(appointments.get(j))) {
                        hasConflict = true;
                        break;
                    }
                }
            }

            if(hasConflict) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments cannot be double booked", ButtonType.OK);
                alert.setTitle("Appointments Error");
                alert.setHeaderText("Appointment collision");
                alert.show();
            } else {
                if(user.getAppointmentCursor() != null) {
                    new AppointmentsDao().update(appointment);
                } else {
                    new AppointmentsDao().add(appointment);
                }

                this.user = new UsersDao().getById(this.user.getId());
                this.appointmentsController.user = this.user;
                this.appointmentsController.initTable(FXCollections.observableList(Arrays.asList(this.user.getAppointments())));

                ((Node)actionEvent.getSource()).getScene().getWindow().hide();
            }

        } else {
            fieldsRequiredLabel.setVisible(true);
        }
    }
}
