package com.danielohagan.webapp.datalayer.dao.implementations;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;
import com.danielohagan.webapp.datalayer.dao.interfaces.IUserDAO;
import com.danielohagan.webapp.datalayer.database.hikari.DataSource;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.IErrorType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String SYSTEM_JDBC_STATUS_COLUMN_NAME =
            "JDBC_STATUS_COLUMN_NAME";
    private static final String SYSTEM_JDBC_CREATION_TIME_COLUMN_NAME =
            "JDBC_CREATION_TIME_COLUMN_NAME";

    public static final String ACCOUNT_TABLE_NAME =
            System.getProperty(SYSTEM_JDBC_ACCOUNT_TABLE_NAME);
    public static final String ID_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_ID_COLUMN_NAME);
    public static final String USERNAME_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_USERNAME_COLUMN_NAME);
    public static final String EMAIL_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_EMAIL_COLUMN_NAME);
    public static final String PASSWORD_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_PASSWORD_COLUMN_NAME);
    public static final String STATUS_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_STATUS_COLUMN_NAME);
    public static final String CREATION_TIME_COLUMN_NAME =
            System.getProperty(SYSTEM_JDBC_CREATION_TIME_COLUMN_NAME);

    public static final int EMAIL_MAX_LENGTH = 32;
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int USERNAME_MIN_LENGTH = 6;

    @Override
    public User getById(int id) {
        User user = null;
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            user = generateUser(resultSet);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        User user = null;
        String sqlStatement =
                "SELECT *" +
                        " FROM " +
                        ACCOUNT_TABLE_NAME +
                        " WHERE " +
                        EMAIL_COLUMN_NAME + " = ?" +
                        " AND " +
                        PASSWORD_COLUMN_NAME + " = ?;";
        ResultSet resultSet;

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            user = generateUser(resultSet);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void updatePassword(int id, String password) {
        String sqlStatement =
                "UPDATE " +
                        ACCOUNT_TABLE_NAME +
                " SET " +
                        PASSWORD_COLUMN_NAME + " = ?" +
                " WHERE "
                        + ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
        String sqlStatement =
                "INSERT INTO " +
                        ACCOUNT_TABLE_NAME +
                        " (" +
                            ID_COLUMN_NAME + ", " +
                            EMAIL_COLUMN_NAME + ", " +
                            USERNAME_COLUMN_NAME + ", " +
                            PASSWORD_COLUMN_NAME + ", " +
                            STATUS_COLUMN_NAME +
                        ")" +
                " VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            //Insert data into prepared statement
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, password);
            preparedStatement.setString(
                    5,
                    user.getUserStatus().getDatabaseEnumStringValue()
            );

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a User account from the database
     *
     * @param id The unique user ID
     */
    @Override
    public void deleteUser(int id) {
        String sqlStatement =
                "DELETE FROM "
                        + ACCOUNT_TABLE_NAME +
                " WHERE "
                        + ID_COLUMN_NAME + " = ?;";

        try(Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        //TODO:: Remove from Chat Sessions Links

        //TODO:: Delete Messages
    }

    @Override
    public boolean exists(int id) {
        String sqlStatement =
                "SELECT *" +
                " FROM "
                        + ACCOUNT_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Map<String, String> getColumnStringsById(
            int id,
            String... columnNames
    ) {
        //TODO:: Check to see if this is working now
        Map<String, String> columnStringsMap = new HashMap<>();
        String sqlStatement = buildSelectByIdSqlRequest(columnNames);

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (columnNames.length > 0 && resultSet.next()) {
                for (String columnName : columnNames) {
                    columnStringsMap.put(
                            columnName,
                            resultSet.getString(columnName)
                    );
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnStringsMap;
    }


    @Override
    public Map<String, Integer> getColumnIntegersById(
            int id,
            String... columnNames
    ) {
        //TODO:: Check to see if this is working now
        Map<String, Integer> columnIntegerMap = new HashMap<>();
        String sqlStatement = buildSelectByIdSqlRequest(columnNames);

        try (Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (columnNames.length > 0) {
                for (String columnName : columnNames) {
                    columnIntegerMap.put(
                            columnName,
                            resultSet.getInt(columnName)
                    );
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnIntegerMap;
    }

    @Override
    public String getColumnString(int id, String columnName) {
        String result = null;

        String sqlStatement =
                "SELECT " +
                        columnName +
                        " FROM " +
                        ACCOUNT_TABLE_NAME +
                        " WHERE " +
                        ID_COLUMN_NAME + " = ?;";


        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getColumnInteger(int id, String columnName) {
        Integer result = null;

        String sqlStatement =
                "SELECT " +
                        columnName +
                        " FROM " +
                        ACCOUNT_TABLE_NAME +
                        " WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            result = resultSet.getInt(columnName);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean isCorrectPassword(int id, String password) {
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE " + ID_COLUMN_NAME + " = ?" +
                " AND " +
                        PASSWORD_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return true;
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void updateUserStatus(int id, UserStatus userStatus) {
        String sqlStatement =
                "UPDATE " +
                        ACCOUNT_TABLE_NAME +
                " SET " +
                        STATUS_COLUMN_NAME + " = ? " +
                "WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(
                    1,
                    userStatus.getDatabaseEnumStringValue()
            );
            preparedStatement.setInt(2, id);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param email
     *
     * @return
     */
    @Override
    public User getUserByEmail(String email) {
        User user = null;
        String sqlStatement =
                "SELECT *" +
                        " FROM " +
                        ACCOUNT_TABLE_NAME +
                        " WHERE " +
                        EMAIL_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            user = generateUser(resultSet);

            //Clean Up
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> getByUsername(String username) {
        List<User> userList = new ArrayList<>();
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE " +
                        USERNAME_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            userList = generateUserList(resultSet);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * Check if an account exists the corresponds with the given email and password
     *
     * @param email The email input
     * @param password The password input
     *
     * @return If an account exists then return true else return false
     */
    @Override
    public boolean exists(String email, String password) {
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE " +
                        EMAIL_COLUMN_NAME + " = ?" +
                " AND " +
                        PASSWORD_COLUMN_NAME + " = ?;";
        ResultSet resultSet;
        boolean accountExists = false;

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            //Check if the result set contains any data,
            //if so then an account with the corresponding inputs exists
            if (resultSet.next()) {
                accountExists = true;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountExists;
    }

    private boolean isEmailTaken(String email) {
        return isColumnRowTaken(EMAIL_COLUMN_NAME, email);
    }

    private boolean isUsernameTaken(String username) {
        return isColumnRowTaken(USERNAME_COLUMN_NAME, username);
    }

    private boolean isColumnRowTaken(
            String column,
            String value
    ) {
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE "
                        + column + " = ?;";
        ResultSet resultSet;
        boolean isTaken = false;

        try (Connection connection = DataSource.getConnection()) {

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);


            preparedStatement.setString(1, value);

            resultSet = preparedStatement.executeQuery();

            //Check if the result set contains any data,
            //if so then an account with the corresponding inputs exists
            if (resultSet.next()) {
                isTaken = true;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isTaken;
    }

    private User generateUser(ResultSet resultSet) {
        User user = null;

        try {
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt(ID_COLUMN_NAME),
                        resultSet.getString(EMAIL_COLUMN_NAME),
                        resultSet.getString(USERNAME_COLUMN_NAME),
                        parseUserStatusFromString(
                                resultSet.getString(STATUS_COLUMN_NAME)
                        ),
                        resultSet.getObject(CREATION_TIME_COLUMN_NAME, LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private List<User> generateUserList(ResultSet resultSet) {
        List<User> userList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt(ID_COLUMN_NAME),
                        resultSet.getString(EMAIL_COLUMN_NAME),
                        resultSet.getString(USERNAME_COLUMN_NAME),
                        parseUserStatusFromString(
                                resultSet.getString(STATUS_COLUMN_NAME)
                        ),
                        resultSet.getObject(CREATION_TIME_COLUMN_NAME, LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * Check the parameters for errors
     *
     * @param email The string from the HTML form Email input
     * @param password The string from the HTML form Password input
     *
     * @return List of errors
     */
    public List<IErrorType> userLoginGetErrors(String email, String password) {
        /*
         * Sort through the parameters to see if the inputs are valid
         * and exist in the database
         */
        List<IErrorType> errorTypeList = new ArrayList<>();

        //Check if the inputs are valid
        errorTypeList.addAll(getEmailPasswordErrors(email, password));

        //Check if the inputs correspond to an account on the database
        errorTypeList.addAll(getAccountConnectionErrors(email, password));

        return errorTypeList;
    }

    public List<IErrorType> changePasswordGetErrors(
            int id,
            String newPassword,
            String newPasswordConfirm,
            String oldPassword
    ) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (getById(id) == null) {
            errorTypeList.add(AccountErrorType.FAILED_TO_RETRIEVE_USER);
        }

        //Find newPassword errors
        if (newPassword == null || newPassword.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_PASSWORD_EMPTY);
        } else {
            newPassword = newPassword.trim();
        }

        //Find newPasswordConfirm errors
        if (newPasswordConfirm == null || newPasswordConfirm.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_PASSWORD_EMPTY);
        } else {
            newPasswordConfirm = newPasswordConfirm.trim();
        }

        //Find oldPassword errors
        if (oldPassword == null || oldPassword.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_PASSWORD_EMPTY);

        } else {
            oldPassword = oldPassword.trim();

            if (passwordContainsIllegalChar(oldPassword)) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER);
            }

            if (oldPassword.length() > PASSWORD_MAX_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_LONG);
            }

            if (oldPassword.length() < PASSWORD_MIN_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_SHORT);
            }
        }

        if (newPassword != null && newPasswordConfirm != null) {
            if (!newPassword.equals(newPasswordConfirm)) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_MISMATCH);
            } else {
                if (newPassword.length() > PASSWORD_MAX_LENGTH) {
                    errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_LONG);
                } else if (newPassword.length() < PASSWORD_MIN_LENGTH) {
                    errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_SHORT);
                }

                if (passwordContainsIllegalChar(newPassword)) {
                    errorTypeList.add(AccountErrorType.INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER);
                }
            }
        }

        return errorTypeList;
    }

    public List<IErrorType> userRegisterGetErrors(
            String username,
            String email,
            String emailConfirm,
            String password,
            String passwordConfirm
    ) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        //Check if the email, emailConfirm, password and passwordConfirm are valid inputs
        errorTypeList.addAll(emailPasswordConfirmationGetErrors(
                email,
                emailConfirm,
                password,
                passwordConfirm
        ));

        //Check if username is a valid input
        errorTypeList.addAll(getUsernameErrors(username));

        if (email != null && !email.isEmpty()) {
            if (isEmailTaken(email)) {
                errorTypeList.add(AccountErrorType.CREATION_EMAIL_TAKEN);
            }
        }

        if (username != null && !username.isEmpty()) {
            if (isUsernameTaken(username)) {
                errorTypeList.add(AccountErrorType.CREATION_USERNAME_TAKEN);
            }
        }

        return errorTypeList;
    }

    /**
     * Check the inputs for errors that would prevent correct use in the database
     *
     * @param email The string from the HTML form Email input
     * @param password The string from the HTML form Password input
     *
     * @return List of errors in the given parameters
     */
    private List<IErrorType> getEmailPasswordErrors(String email, String password) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        //Find email errors
        if (email == null || email.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_EMAIL_EMPTY);
        } else {
            email = email.trim();
            if (email.length() > EMAIL_MAX_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_EMAIL_TOO_LONG);
            }

            if (email.length() < EMAIL_MIN_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_EMAIL_TOO_SHORT);
            }

            //TODO:: Check for invalid characters
            if (emailContainsIllegalChar(email)) {

            }
        }

        //Find password errors
        if (password == null || password.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_PASSWORD_EMPTY);
        } else {
            password = password.trim();

            if (password.length() > PASSWORD_MAX_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_LONG);
            }

            if (password.length() < PASSWORD_MIN_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_TOO_SHORT);
            }

            //TODO:: Check for invalid characters
            if (passwordContainsIllegalChar(password)) {

            }
        }

        return errorTypeList;
    }

    private List<IErrorType> getUsernameErrors(String username) {

        List<IErrorType> errorTypeList = new ArrayList<>();

        if (username == null || username.isEmpty()) {
            errorTypeList.add(AccountErrorType.INPUT_USERNAME_EMPTY);
        } else {
            username = username.trim();

            if (username.length() > USERNAME_MAX_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_USERNAME_TOO_LONG);
            }

            if (username.length() < USERNAME_MIN_LENGTH) {
                errorTypeList.add(AccountErrorType.INPUT_USERNAME_TOO_SHORT);
            }
        }

        //TODO:: Check for invalid characters

        return errorTypeList;
    }

    private boolean passwordContainsIllegalChar(String password) {
        //TODO:: This
        //Don't forget to disallow spaces

        return false;
    }

    private boolean emailContainsIllegalChar(String email) {
        //TODO:: This
        //Don't forget to disallow spaces

        return false;
    }

    private List<IErrorType> emailPasswordConfirmationGetErrors(
            String email,
            String emailConfirm,
            String password,
            String passwordConfirm
    ) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        //Check the Email and Password are valid inputs
        errorTypeList.addAll(getEmailPasswordErrors(email, password));

        //Check the Email and Password confirmations are valid inputs
        errorTypeList.addAll(getEmailPasswordErrors(emailConfirm, passwordConfirm));

        //Check the email and password are the same as the confirmation inputs
        if (!email.equals(emailConfirm)) {
            errorTypeList.add(AccountErrorType.INPUT_EMAIL_MISMATCH);
        }

        if (!password.equals(passwordConfirm)) {
            errorTypeList.add(AccountErrorType.INPUT_PASSWORD_MISMATCH);
        }

        return errorTypeList;
    }

    /**
     * Check the inputs for errors that occur when
     *  using the inputs to find an account on the database
     *
     * @param email
     * @param password
     *
     * @return List of errors
     */
    private List<IErrorType> getAccountConnectionErrors(
            String email,
            String password
    ) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (!exists(email, password)) {
            if (isEmailTaken(email)) {
                errorTypeList.add(AccountErrorType.INPUT_PASSWORD_INCORRECT);
            } else {
                errorTypeList.add(AccountErrorType.LOGIN_NO_CORRESPONDING_ACCOUNT);
            }
        }

        return errorTypeList;
    }

    public UserStatus parseUserStatusFromString(String stringValue) {

        if (stringValue != null && !stringValue.isEmpty()) {
            for (UserStatus userStatus : UserStatus.values()) {
                if (userStatus.getDatabaseEnumStringValue().equals(stringValue)) {
                    return userStatus;
                }
            }
        }

        return UserStatus.NULL;
    }

    private String buildSelectByIdSqlRequest(String... columnNames) {
        StringBuilder sqlStatementBuilder = null;
        String sqlStatement = null;

        sqlStatementBuilder = new StringBuilder(
                "SELECT "
        );

        for (int i = 0; i < columnNames.length; i++) {
            sqlStatementBuilder.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                sqlStatementBuilder.append(", ");
            } else {
                sqlStatementBuilder.append(" ");
            }
        }

        sqlStatementBuilder.append(
                " FROM " +
                        ACCOUNT_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;"
        );

        sqlStatement = sqlStatementBuilder.toString();

        return sqlStatement;
    }
}