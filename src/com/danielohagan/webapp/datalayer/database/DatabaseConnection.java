package com.danielohagan.webapp.datalayer.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String SYSTEM_JDBC_ADMIN_USERNAME = "JDBC_ADMIN_USERNAME";
    public static final String SYSTEM_JDBC_ADMIN_PASSWORD = "JDBC_ADMIN_PASSWORD";
    public static final String SYSTEM_JDBC_LOCALHOST_URL = "JDBC_LOCALHOST_URL";

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
}