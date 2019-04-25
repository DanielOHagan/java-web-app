package webapp.datalayer.account;

import webapp.applayer.account.User;
import webapp.datalayer.database.DatabaseConnection;
import webapp.error.AccountErrorType;
import webapp.error.ErrorType;
import webapp.error.IErrorType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUser {

    private static final String SYSTEM_JDBC_ACCOUNT_TABLE_NAME =
            "JDBC_ACCOUNT_TABLE_NAME";
    private static final String SYSTEM_JDBC_ID_COLUMN_NAME =
            "JDBC_ID_COLUMN_NAME";
    private static final String SYSTEM_JDBC_USERNAME_COLUMN_NAME =
            "JDBC_USERNAME_COLUMN_NAME";
    private static final String SYSTEM_JDBC_EMAIL_COLUMN_NAME =
            "JDBC_EMAIL_COLUMN_NAME";
    private static final String SYSTEM_JDBC_PASSWORD_COLUMN_NAME =
            "JDBC_PASSWORD_COLUMN_NAME";

    private static final String ACCOUNT_TABLE_NAME =
            System.getProperty(SYSTEM_JDBC_ACCOUNT_TABLE_NAME);
    private static final String ID_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_ID_COLUMN_NAME);
    private static final String USERNAME_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_USERNAME_COLUMN_NAME);
    private static final String EMAIL_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_EMAIL_COLUMN_NAME);
    private static final String PASSWORD_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_PASSWORD_COLUMN_NAME);

    public static final int EMAIL_MAX_LENGTH = 32;
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int USERNAME_MIN_LENGTH = 6;

    /**
     *
     *
     * @param id
     *
     * @return
     */
    public static User getUserFromDatabaseById(int id) {
        User user = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + ID_COLUMN_NAME + " = \"" + id +
                        "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Extract the username from the result set
                if (resultSet.next()) {
                    user = new User(
                            id,
                            resultSet.getString(EMAIL_COLUMN_NAME),
                            resultSet.getString(USERNAME_COLUMN_NAME)
                    );
                }

                //Clean Up
                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }

    /**
     *
     *
     * @param email
     *
     * @return
     */
    public static User getUserFromDatabaseByEmail(String email) {
        User user = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                        "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Extract the username from the result set
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getInt(ID_COLUMN_NAME),
                            email,
                            resultSet.getString(USERNAME_COLUMN_NAME)
                    );
                }

                //Clean Up
                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }

    /**
     * Check if an account exists the corresponds with the given email and password
     *
     * @param email The email input
     * @param password The password input
     *
     * @return If an account exists then return true else return false
     */
    private static boolean accountExists(String email, String password) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                        "\" AND " + PASSWORD_COLUMN_NAME + " = \"" +
                        password + "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Extract the username from the result set
                if (resultSet.next()) {
                    return true;
                }

                //Clean Up
                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * Check the parameters for errors
     *
     * @param email The string from the HTML form Email input
     * @param password The string from the HTML form Password input
     *
     * @return If no errors then returns ErrorType.NO_ERROR,
     *  else it returns a specific error as to why the request failed
     */
    public static IErrorType userLoginGetErrorType(String email, String password) {
        /*
         * Sort through the parameters to see if the inputs are valid and exist in the database
         */
        IErrorType errorType;

        //Check if the inputs are valid
        if (
                (errorType = inputsGetErrorType(email, password))
                        != ErrorType.NO_ERROR
        ) {
            return errorType;
        }

        //Check if the inputs correspond to an account on the database
        if (
                (errorType = getAccountConnectionErrorType(email, password))
                        != ErrorType.NO_ERROR
        ) {
            return errorType;
        }

        return ErrorType.NO_ERROR;
    }

    /**
     * Check the inputs for errors that would prevent correct use in the database
     *
     * @param email The string from the HTML form Email input
     * @param password The string from the HTML form Password input
     *
     * @return If no errors then returns ErrorType.NO_ERROR,
     *  else it returns a specific error as to why the request failed
     */
    private static IErrorType inputsGetErrorType(String email, String password) {

        if (email == null || email.isEmpty()) {
            return AccountErrorType.INPUT_EMAIL_EMPTY;
        }

        if (password == null || password.isEmpty()) {
            return AccountErrorType.INPUT_PASSWORD_EMPTY;
        }

        if (email.length() > EMAIL_MAX_LENGTH) {
            return AccountErrorType.INPUT_EMAIL_TOO_LONG;
        }

        if (email.length() < EMAIL_MIN_LENGTH) {
            return AccountErrorType.INPUT_EMAIL_TOO_SHORT;
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_LONG;
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_SHORT;
        }

        return ErrorType.NO_ERROR;
    }

    /**
     * Check the inputs for errors that occur when
     *  using the inputs to find an account on the database
     *
     * @param email
     * @param password
     *
     * @return If no errors then returns ErrorType.NO_ERROR,
     *      *  else it returns a specific error as to why the request failed
     */
    private static IErrorType getAccountConnectionErrorType(
            String email,
            String password
    ) {
        if (!accountExists(email, password)) {
            return AccountErrorType.LOGIN_NO_CORRESPONDING_ACCOUNT;
        }

        return ErrorType.NO_ERROR;
    }
}