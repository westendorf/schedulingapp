package io.github.westendorf.controller;

import io.github.westendorf.Main;
import io.github.westendorf.dao.AppointmentsDao;
import io.github.westendorf.dto.Appointment;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.view.TopMenu;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController extends Controller {

    @FXML
    private TopMenu topMenu;
    @FXML
    private Label welcomeUserLabel;

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
     * Initializes the window based on the user
     *
     * @param user
     * @param dto
     */
    public void start(User user, DataTransferObject dto) {
        this.user = user;
        welcomeUserLabel.setText(Main.getString("welcome_user") + " " + user.getUsername() + "!");

        List<Appointment> appointmentsNow = appointmentsIn15Minutes();
        if(appointmentsNow.size() > 0) showAppointmentReminders(appointmentsNow);
        else showNoUpcomingAppointments();
    }

    /**
     * Shows an alert window that indicates that there are no upcoming appointments
     */
    private void showNoUpcomingAppointments() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "No upcoming appointments", ButtonType.OK);
        alert.setTitle("Appointments");
        alert.setHeaderText("Upcoming Appointments");
        alert.show();
    }

    /**
     * Calculates a list of appointments that are in 15 minutes
     *
     * @return List appointments in 15 minutes
     */
    public List<Appointment> appointmentsIn15Minutes() {
        List<Appointment> appointments = new ArrayList<>();
        List<Appointment> appntsInDb = new AppointmentsDao().getAll();
        appntsInDb.forEach(a -> {
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            long secondsAway = (a.getStart().getTime() / 1000) - (now.getTime() / 1000);
            if(secondsAway <= 900 && secondsAway >=0) appointments.add(a);
        });
        return appointments;
    }

    /**
     * For each appointment in a list, a reminder is shown with how long it is until the start of it
     * @param appointments
     */
    public void showAppointmentReminders(List<Appointment> appointments) {
        appointments.forEach(a -> {
            long inMins = (a.getStart().getTime() / 1000 - Timestamp.valueOf(LocalDateTime.now()).getTime() / 1000) / 60;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment ID " + a.getId() + ", " + a.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy h:m a")), ButtonType.OK);
            alert.setTitle(Main.getString("appnt_reminder_title"));
            alert.setHeaderText("Appointment in "+inMins+" minutes");
            alert.show();
        });

    }

    /**
     * Required to set the user variable in the top menu
     *
     * @return
     */
    public TopMenu getTopMenu() {
        return this.topMenu;
    }
}
