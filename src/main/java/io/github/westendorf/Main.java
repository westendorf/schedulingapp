package io.github.westendorf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {
    private static ResourceBundle bundle;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle("schedulingapp", locale);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/log_in_form.fxml")));
        primaryStage.setTitle(getString("log_in_window_title"));
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

}
