package com.danielohagan.webapp.datalayer.database.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String JDBC_ADMIN_USERNAME = "JDBC_ADMIN_USERNAME";
    private static final String JDBC_ADMIN_PASSWORD = "JDBC_ADMIN_PASSWORD";
    private static final String LOCALHOST_URL = "JDBC_LOCALHOST_URL";

    private static HikariConfig mConfig;
    private static HikariDataSource mDataSource;

    static {
        mConfig = new HikariConfig();

        mConfig.setJdbcUrl(System.getProperty(LOCALHOST_URL));
        mConfig.setDriverClassName(JDBC_DRIVER);
        mConfig.setUsername(System.getProperty(JDBC_ADMIN_USERNAME));
        mConfig.setPassword(System.getProperty(JDBC_ADMIN_PASSWORD));
        mConfig.addDataSourceProperty("cachePrepStmts", "true");
        mConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        mConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        mDataSource = new HikariDataSource(mConfig);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return mDataSource.getConnection();
    }

    public static void close() {
        if (mDataSource != null) {
            mDataSource.close();
        }
    }
}