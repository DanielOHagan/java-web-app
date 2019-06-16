package com.danielohagan.webapp.datalayer.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractConnection {

    protected Connection mConnection;

    abstract public Connection getConnection() throws SQLException;
    abstract public Connection getConnection(String username, String password) throws SQLException;
    abstract public void initConnection();
    abstract public void initConnection(String username, String password);

}