package io.github.westendorf.view;

import io.github.westendorf.controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TopMenu extends AnchorPane {

    private MenuController menuController;

    public TopMenu() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Node node = loader.load();
            this.getChildren().add(node);
            this.menuController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MenuController getController() {
        return this.menuController;
    }
}
