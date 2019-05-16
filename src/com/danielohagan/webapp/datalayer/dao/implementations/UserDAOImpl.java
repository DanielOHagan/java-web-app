package com.danielohagan.webapp.datalayer.dao.implementations;

import com.danielohagan.webapp.datalayer.dao.interfaces.IUserDAO;
import com.danielohagan.webapp.datalayer.database.DatabaseConnection;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.error.AccountErrorType;
import com.danielohagan.webapp.error.ErrorType;
import com.danielohagan.webapp.error.IErrorType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImpl implements IUserDAO {

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

    @Override
    public User getById(int id) {
        User user = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );

        //Get the username corresponding to account_THINKTHISSHOULDGO with the given email and password
        if (connection != null) {
            try {
                user = getUser(connection, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        User user = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try {
                user = getUser(connection, email, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    @Override
    public void updatePassword(int id, String password) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;

        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "UPDATE " + ACCOUNT_TABLE_NAME + " SET " +
                        PASSWORD_COLUMN_NAME + " = '" + password + "' WHERE " + ID_COLUMN_NAME +
                        " = " + id + ";";

                statement.execute(sqlStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Store an instance of the User in the database
     *
     * @param user The User entity of the newly created account.
     * @param password The User's password, passed separately so it
     *                 is lost after it is stored in the database
     */
    @Override
    public void createNewUser(User user, String password) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;

        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "INSERT INTO " + ACCOUNT_TABLE_NAME + " (" +
                        EMAIL_COLUMN_NAME + ", " + USERNAME_COLUMN_NAME + ", " +
                        PASSWORD_COLUMN_NAME + ")" +
                        " VALUES ('" + user.getEmail() + "', '" + user.getUsername() +
                        "', '" + password + "');";

                statement.execute(sqlStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Delete a User account from the database
     *
     * @param id The unique user ID
     */
    @Override
    public void deleteUser(int id) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;

        if (connection != null) {
            try (Statement statement = connection.createStatement()) {

                sqlStatement = "DELETE FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + ID_COLUMN_NAME + " = " + id + ";";

                statement.execute(sqlStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     *
     * @param email
     *
     * @return
     */
    public User getUserByEmail(String email) {
        User user = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try {
                user = getUser(connection, email);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    /**
     * Check if an account_THINKTHISSHOULDGO exists the corresponds with the given email and password
     *
     * @param email The email input
     * @param password The password input
     *
     * @return If an account_THINKTHISSHOULDGO exists then return true else return false
     */
    private boolean accountExists(String email, String password) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;
        boolean accountExists = false;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                        "\" AND " + PASSWORD_COLUMN_NAME + " = \"" +
                        password + "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Check if the result set contains any data,
                //if so then an account with the corresponding inputs exists
                if (resultSet.next()) {
                    accountExists = true;
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

        return accountExists;
    }

    private boolean isEmailTaken(String email) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;
        boolean accountExists = false;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                        "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Check if the result set contains any data,
                //if so then an account with the corresponding inputs exists
                if (resultSet.next()) {
                    accountExists = true;
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

        return accountExists;
    }

    private boolean isUsernameTaken(String username) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection(
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_USERNAME),
                        System.getProperty(DatabaseConnection.SYSTEM_JDBC_ADMIN_PASSWORD)
                );
        String sqlStatement;
        ResultSet resultSet;
        boolean accountExists = false;

        //Get the username corresponding to account with the given email and password
        if (connection != null) {
            try(Statement statement = connection.createStatement()) {

                sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                        " WHERE " + USERNAME_COLUMN_NAME + " = \"" + username +
                        "\";";

                resultSet = statement.executeQuery(sqlStatement);

                //Check if the result set contains any data,
                //if so then an account with the corresponding inputs exists
                if (resultSet.next()) {
                    accountExists = true;
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

        return accountExists;
    }

    private User getUser(Connection connection, String email, String password) throws SQLException {
        User user = null;

        try(Statement statement = connection.createStatement()) {
            String sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                    " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                    "\" AND " + PASSWORD_COLUMN_NAME + " = \"" + password +
                    "\";";

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            user = generateUser(resultSet);

            //Clean Up
            resultSet.close();
            statement.close();
            connection.close();
        }

        return user;
    }

    private User getUser(Connection connection, int id) throws SQLException {
        User user = null;

        try(Statement statement = connection.createStatement()) {
            String sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                    " WHERE " + ID_COLUMN_NAME + " = \"" + id +
                    "\";";

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            user = generateUser(resultSet);

            //Clean Up
            resultSet.close();
            statement.close();
            connection.close();
        }

        return user;
    }

    private User getUser(Connection connection, String email) throws SQLException {
        User user = null;

        try(Statement statement = connection.createStatement()) {
            String sqlStatement = "SELECT * FROM " + ACCOUNT_TABLE_NAME +
                    " WHERE " + EMAIL_COLUMN_NAME + " = \"" + email +
                    "\";";

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            user = generateUser(resultSet);

            //Clean Up
            resultSet.close();
            statement.close();
            connection.close();
        }

        return user;
    }

    private User generateUser(ResultSet resultSet) {
        User user = null;

        try {
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt(ID_COLUMN_NAME),
                        resultSet.getString(EMAIL_COLUMN_NAME),
                        resultSet.getString(USERNAME_COLUMN_NAME)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
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
    public IErrorType userLoginGetErrorType(String email, String password) {
        /*
         * Sort through the parameters to see if the inputs are valid and exist in the database
         */
        IErrorType errorType;

        //Check if the inputs are valid
        if (
                (errorType = getEmailPasswordErrorType(email, password))
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

    public IErrorType changePasswordGetErrorType(
            int id,
            String newPassword,
            String newPasswordConfirm,
            String oldPassword
    ) {

        if (getById(id) == null) {
            return AccountErrorType.FAILED_TO_RETRIEVE_USER;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            return AccountErrorType.INPUT_PASSWORD_EMPTY;
        }

        if (newPasswordConfirm == null || newPasswordConfirm.isEmpty()) {
            return AccountErrorType.INPUT_PASSWORD_EMPTY;
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            return AccountErrorType.INPUT_PASSWORD_EMPTY;
        }

        newPassword = newPassword.trim();
        newPasswordConfirm = newPasswordConfirm.trim();
        oldPassword = oldPassword.trim();

        if (!newPassword.equals(newPasswordConfirm)) {
            return AccountErrorType.INPUT_PASSWORD_MISMATCH;
        }

        if (passwordCotainsIllegalChar(newPassword)) {
            return AccountErrorType.INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER;
        }

        if (passwordCotainsIllegalChar(oldPassword)) {
            return AccountErrorType.INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER;
        }

        if (newPassword.length() > PASSWORD_MAX_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_LONG;
        }

        if (newPassword.length() < PASSWORD_MIN_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_SHORT;
        }

        if (oldPassword.length() > PASSWORD_MAX_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_LONG;
        }

        if (oldPassword.length() < PASSWORD_MIN_LENGTH) {
            return AccountErrorType.INPUT_PASSWORD_TOO_SHORT;
        }

        return ErrorType.NO_ERROR;
    }

    public IErrorType userRegisterGetErrorType(
            String username,
            String email,
            String emailConfirm,
            String password,
            String passwordConfirm
    ) {
        IErrorType errorType;

        //Check if the email, emailConfirm, password and passwordConfirm are valid inputs
        errorType = emailPasswordConfirmationGetErrorType(
                email,
                emailConfirm,
                password,
                passwordConfirm
        );
        if (errorType != ErrorType.NO_ERROR) {
            return errorType;
        }

        //Check if username is a valid input
        errorType = getUsernameErrorType(username);
        if (errorType != ErrorType.NO_ERROR) {
            return errorType;
        }

        if (isEmailTaken(email)) {
            return AccountErrorType.CREATION_EMAIL_TAKEN;
        }

        if (isUsernameTaken(username)) {
            return AccountErrorType.CREATION_USERNAME_TAKEN;
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
    private IErrorType getEmailPasswordErrorType(String email, String password) {

        if (email == null || email.isEmpty()) {
            return AccountErrorType.INPUT_EMAIL_EMPTY;
        }

        if (password == null || password.isEmpty()) {
            return AccountErrorType.INPUT_PASSWORD_EMPTY;
        }

        email = email.trim();
        password = password.trim();

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

        //TODO:: Check for invalid characters
        if (emailCotainsIllegalChar(email)) {

        }

        if (passwordCotainsIllegalChar(password)) {

        }

        return ErrorType.NO_ERROR;
    }

    private IErrorType getUsernameErrorType(String username) {

        if (username == null || username.isEmpty()) {
            return AccountErrorType.INPUT_USERNAME_EMPTY;
        }

        username = username.trim();

        if (username.length() > USERNAME_MAX_LENGTH) {
            return AccountErrorType.INPUT_USERNAME_TOO_LONG;
        }

        if (username.length() < USERNAME_MIN_LENGTH) {
            return AccountErrorType.INPUT_USERNAME_TOO_SHORT;
        }

        //TODO:: Check for invalid characters

        return ErrorType.NO_ERROR;
    }

    private boolean passwordCotainsIllegalChar(String password) {


        return false;
    }

    private boolean emailCotainsIllegalChar(String email) {


        return false;
    }

    private IErrorType emailPasswordConfirmationGetErrorType(
            String email,
            String emailConfirm,
            String password,
            String passwordConfirm
    ) {
        IErrorType errorType;

        //Check the Email and Password are valid inputs
        errorType = getEmailPasswordErrorType(email, password);
        if (errorType != ErrorType.NO_ERROR) {
            return errorType;
        }

        //Check the Email and Password confirmations are valid inputs
        errorType = getEmailPasswordErrorType(emailConfirm, passwordConfirm);
        if (errorType != ErrorType.NO_ERROR) {
            return errorType;
        }

        //Check the email and password are the same as the confirmation inputs
        if (!email.equals(emailConfirm)) {
            return AccountErrorType.INPUT_EMAIL_MISMATCH;
        }

        if (!password.equals(passwordConfirm)) {
            return AccountErrorType.INPUT_PASSWORD_MISMATCH;
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
    private IErrorType getAccountConnectionErrorType(
            String email,
            String password
    ) {
        if (!accountExists(email, password)) {
            return AccountErrorType.LOGIN_NO_CORRESPONDING_ACCOUNT;
        }

        return ErrorType.NO_ERROR;
    }
}