package io.github.westendorf.controller;

import io.github.westendorf.dto.DataTransferObject;
import io.github.westendorf.dto.User;
import javafx.fxml.Initializable;

public abstract class Controller implements Initializable {

    protected User user;

    /**
     * This allows subclasses to have a user object and DTO
     *
     * @param user
     * @param dto
     */
    public abstract void start(User user, DataTransferObject dto);

    /**
     * This is a convenience method in case a DTO is not necessary
     *
     * @param user
     */
    public void start(User user) {
        this.user = user;
        this.start(user, null);
    }
}
