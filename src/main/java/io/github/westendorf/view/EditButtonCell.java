package io.github.westendorf.view;

import io.github.westendorf.dto.DataTransferObject;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class EditButtonCell extends TableCell<DataTransferObject, String> {


    public EditButtonCell() {
        setGraphic(new Button("Edit"));
    }
}
