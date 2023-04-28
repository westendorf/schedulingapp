package io.github.westendorf.dto;

public abstract class DataTransferObject {
    private int id;

    public DataTransferObject(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
