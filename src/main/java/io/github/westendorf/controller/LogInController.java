package io.github.westendorf.controller;

import io.github.westendorf.Main;
import io.github.westendorf.dao.UsersDao;
import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import io.github.westendorf.security.Passwords;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class LogInController extends Controller {

    @FXML
    private Label zoneIdLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    /**
     * Initializes the window
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIdLabel.setText(ZoneId.systemDefault().toString());
        usernameField.setPromptText(Main.getString("username_hint"));
        passwordField.setPromptText(Main.getString("password_hint"));
        loginButton.setText(Main.getString("log_in_btn_txt"));
    }

    /**
     * Event handler for when the login button is pressed
     *
     * @param actionEvent
     */
    public void logIn(ActionEvent actionEvent) {
        boolean loginSuccess = false;
        if(Passwords.checkPassword(usernameField.getText(), passwordField.getText())) {
            loginSuccess = true;
            User userToPass = new UsersDao().getAll().stream().filter(u -> u.getUsername().equals(usernameField.getText())).findFirst().get();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/dashboard.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Dashboard");
                stage.show();
                DashboardController dc = loader.getController();
                dc.getTopMenu().getController().start(userToPass);
                dc.start(userToPass);
                dc.getTopMenu().getController().getDashboardMenuItem().setDisable(true);
                ((Node)actionEvent.getSource()).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, Main.getString("login_error_content"), ButtonType.CLOSE);
            alert.setTitle(Main.getString("login_error_title"));
            alert.setHeaderText(Main.getString("login_error_header"));
            alert.show();
        }


        try {
            String filePath = System.getProperty("user.dir") + "/login_activity.txt";
            String line = ZonedDateTime.now() + " success=" + (loginSuccess?"true":"false" + "\n");
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Empty stub for required superclass abstract function
     * @param user
     * @param dto
     */
    @Override
    public void start(User user, DataTransferObject dto) {

    }
}
