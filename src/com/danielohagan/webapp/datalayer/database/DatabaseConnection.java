package com.danielohagan.webapp.datalayer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection extends AbstractConnection {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SYSTEM_JDBC_ADMIN_USERNAME = "JDBC_ADMIN_USERNAME";
    private static final String SYSTEM_JDBC_ADMIN_PASSWORD = "JDBC_ADMIN_PASSWORD";
    private static final String SYSTEM_JDBC_LOCALHOST_URL = "JDBC_LOCALHOST_URL";

    public static Connection getDatabaseConnection() {
        //Connect to the database as admin
        return getDatabaseConnection(
                System.getProperty(SYSTEM_JDBC_ADMIN_USERNAME),
                System.getProperty(SYSTEM_JDBC_ADMIN_PASSWORD)
        );
    }

    public static Connection getDatabaseConnection(
            String username,
            String password
    ) {
        //Connect to the database as admin
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);

            String dbUrl = System.getProperty(SYSTEM_JDBC_LOCALHOST_URL);
            String dbConnectionString =
                    dbUrl + "?user=" + username + "&password=" + password;

            connection = DriverManager.getConnection(dbConnectionString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (mConnection == null || mConnection.isClosed()) {
            mConnection = getDatabaseConnection();
        }

        return mConnection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (mConnection == null || mConnection.isClosed()) {
            mConnection = getDatabaseConnection(username, password);
        }

        return mConnection;
    }

    @Override
    public void initConnection() {
        mConnection = getDatabaseConnection();
    }

    @Override
    public void initConnection(String username, String password) {
        mConnection = getDatabaseConnection(username, password);
    }
}