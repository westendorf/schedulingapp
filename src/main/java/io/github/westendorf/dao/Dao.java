package io.github.westendorf.dao;

import io.github.westendorf.jdbc.JDBC;

import java.sql.Connection;
import java.util.List;

public abstract class Dao<DataTransferObject extends io.github.westendorf.dto.DataTransferObject> {

    private JDBC conn;

    /**
     * Setups a new database connection using the JDBC class
     *
     * @return Connection
     */
    public Connection getNewConnection() {
        if(conn == null) {
            conn = new JDBC();
        }
        return conn.getNewConnection();
    }

    /**
     * Gets the last connection from the JDBC class, closed or not
     *
     * @return Connection
     */
    public Connection getLastConnection() {
        return conn.getLastConnection();
    }

    /**
     * Closes the connection gained from Dao.getNewConnection()
     */
    public void closeConnection() {
        if(conn != null) {
            conn.closeConnection();
        }
    }

    /**
     * Abstract stub for adding the data from a DataTransferObject to the database using a connection from the JDBC class
     *
     * @param dataTransferObject
     * @return
     */
    public abstract boolean add(DataTransferObject dataTransferObject);

    /**
     * Abstract stub for updating the data from a DataTransferObject to the database using a connection from the JDBC class
     *
     * @param dataTransferObject
     * @return boolean successful update
     */
    public abstract boolean update(DataTransferObject dataTransferObject);

    /**
     * Abstract stub for deleting the data from a DataTransferObject to the database using a connection from the JDBC class
     *
     * @param dataTransferObject
     * @return boolean successful deletion
     */
    public abstract boolean delete(DataTransferObject dataTransferObject);

    /**
     * Retrieves a DataTransferObject using its id from the database using a connection from the JDBC class
     *
     * @param id
     * @return DataTransferObject
     */
    public abstract DataTransferObject getById(int id);

    /**
     * Retrieves all DataTransferObject equivalents from the database using a connection from the JDBC class
     *
     * @return List of DataTransferObjects
     */
    public abstract List<DataTransferObject> getAll();
}
