package com.danielohagan.webapp.datalayer.dao.implementations;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;
import com.danielohagan.webapp.datalayer.dao.interfaces.IChatSessionDAO;
import com.danielohagan.webapp.datalayer.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSessionDAOImpl implements IChatSessionDAO {

    //TODO:: Environment variables

    public static final String CHAT_SESSION_TABLE_NAME = "chat_session_table";
    public static final String CHAT_SESSION_ID_COLUMN_NAME = "session_id";
    public static final String CHAT_SESSION_NAME_COLUMN_NAME = "session_name";
    public static final String CHAT_SESSION_CREATION_TIME_COLUMN_NAME = "creation_time";

    public static final String LINK_TABLE_NAME = "chat_session_account_link_table";
    public static final String LINK_ID_COLUMN_NAME = "link_id";
    public static final String LINK_ACCOUNT_ID_COLUMN_NAME = "account_id";
    public static final String LINK_CHAT_SESSION_ID_COLUMN_NAME = "chat_session_id";
    public static final String LINK_PERMISSION_LEVEL_COLUMN_NAME = "permission_level";

    @Override
    public void createNewSession(ChatSession chatSession, int creatorId) {

    }

    @Override
    public void deleteSession(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        MessageDAOImpl messageDAO = new MessageDAOImpl();

        //Remove Chat Session
        deleteRowsByCondition(
                connection,
                CHAT_SESSION_TABLE_NAME,
                CHAT_SESSION_ID_COLUMN_NAME,
                sessionId
        );

        //Remove Messages
        messageDAO.deleteMessageBySession(sessionId);

        //Remove Session-Account Links
        deleteRowsByCondition(
                connection,
                LINK_TABLE_NAME,
                LINK_CHAT_SESSION_ID_COLUMN_NAME,
                sessionId
        );
    }

    @Override
    public void updateSessionName(int sessionId, String name) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "UPDATE " +
                        CHAT_SESSION_TABLE_NAME +
                " SET " +
                        CHAT_SESSION_NAME_COLUMN_NAME + " = ?" +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?;";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, sessionId);

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a User to a Chat Session, assuming that the User is not already in the Chat Session
     *
     * @param sessionId Target Chat Session to add User
     * @param userId    Target User to add to Chat Session
     */
    @Override
    public void addUserToSession(int sessionId, int userId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();

        addUserToSession(connection, sessionId, userId);
    }

    /**
     * Removes a user from the Chat Session
     *
     * @param sessionId ID of the target Chat Session
     * @param userId    ID of the target User to be removed
     */
    @Override
    public void removeUserFromSession(int sessionId, int userId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "DELETE *" +
                " FROM " +
                        LINK_TABLE_NAME +
                " WHERE " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        LINK_ACCOUNT_ID_COLUMN_NAME + " = ?;";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, userId);

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeLinksByUser(int userId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();

        deleteRowsByCondition(
                connection,
                LINK_TABLE_NAME,
                LINK_ACCOUNT_ID_COLUMN_NAME,
                userId
        );
    }

    @Override
    public void removeLinksBySession(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();

        deleteRowsByCondition(
                connection,
                LINK_TABLE_NAME,
                LINK_CHAT_SESSION_ID_COLUMN_NAME,
                sessionId
        );
    }

    @Override
    public void updateUserPermissionLevel(
            int sessionId,
            int userId,
            ChatPermissionLevel chatPermissionLevel
    ) {

    }

    @Override
    public ChatPermissionLevel getUserPermissionLevel(int sessionId, int userId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " +
                        LINK_PERMISSION_LEVEL_COLUMN_NAME +
                " FROM " +
                        LINK_TABLE_NAME +
                " WHERE " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        LINK_ACCOUNT_ID_COLUMN_NAME + " = ?;";

        ChatPermissionLevel permissionLevel = ChatPermissionLevel.NULL;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                permissionLevel = parsePermissionLevelFromString(
                        resultSet.getString(LINK_PERMISSION_LEVEL_COLUMN_NAME)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissionLevel;
    }

    @Override
    public boolean exists(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " +
                        CHAT_SESSION_ID_COLUMN_NAME +
                " FROM "
                        + CHAT_SESSION_TABLE_NAME +
                " WHERE "
                        + CHAT_SESSION_ID_COLUMN_NAME + " = ?;";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    public Map<String, Integer> getColumnIntegersById(int id,String tableName, String... columnNames) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        Map<String, Integer> columnIntegerMap = new HashMap<>();
        String sqlStatement = buildSelectByIdSqlRequest(tableName, columnNames);

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement.toString())
        ) {
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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnIntegerMap;
    }

    @Override
    public Map<String, String> getColumnStringsById(int id, String tableName, String... columnNames) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        Map<String, String> columnStringsMap = new HashMap<>();
        String sqlStatement = buildSelectByIdSqlRequest(tableName, columnNames);

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (columnNames.length > 0) {
                for (String columnName : columnNames) {
                    columnStringsMap.put(
                            columnName,
                            resultSet.getString(columnName)
                    );
                }
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnStringsMap;
    }

    @Override
    public boolean userIsInChatSession(int userId, int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " +
                        LINK_ID_COLUMN_NAME +
                " FROM " +
                        LINK_TABLE_NAME +
                " WHERE " +
                        LINK_ACCOUNT_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME + " = ?;";
        boolean inSession = false;
        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                resultSet.close();
                inSession = true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return inSession;
    }

    @Override
    public List<ChatSessionUser> getUserListBySession(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME + "." + UserDAOImpl.ID_COLUMN_NAME + ", " +
                        LINK_PERMISSION_LEVEL_COLUMN_NAME + ", " +
                        UserDAOImpl.EMAIL_COLUMN_NAME + ", " +
                        UserDAOImpl.USERNAME_COLUMN_NAME + ", " +
                        UserDAOImpl.STATUS_COLUMN_NAME + ", " +
                        UserDAOImpl.CREATION_TIME_COLUMN_NAME + ", " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME +
                " FROM " +
                        LINK_TABLE_NAME +
                " INNER JOIN " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME +
                " ON " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME + "." + UserDAOImpl.ID_COLUMN_NAME +
                    " = " +
                        LINK_TABLE_NAME + "." + LINK_ACCOUNT_ID_COLUMN_NAME +
                " WHERE " +
                        LINK_TABLE_NAME + "." + LINK_CHAT_SESSION_ID_COLUMN_NAME + " = ?;";

        List<ChatSessionUser> chatSessionUserList = null;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            chatSessionUserList = generateChatSessionUserList(resultSet);

            connection.close();
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatSessionUserList;
    }

    @Override
    public List<ChatSessionUser> getUserListByPermission(
            int sessionId,
            ChatPermissionLevel chatPermissionLevel
    ) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();

        String sqlStatement =
                "SELECT " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME + "." + UserDAOImpl.ID_COLUMN_NAME + ", " +
                        LINK_PERMISSION_LEVEL_COLUMN_NAME + ", " +
                        UserDAOImpl.EMAIL_COLUMN_NAME + ", " +
                        UserDAOImpl.USERNAME_COLUMN_NAME + ", " +
                        UserDAOImpl.STATUS_COLUMN_NAME + ", " +
                        UserDAOImpl.CREATION_TIME_COLUMN_NAME + ", " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME + ", " +
                " FROM " +
                        LINK_TABLE_NAME +
                " INNER JOIN " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME +
                " ON " +
                        UserDAOImpl.ACCOUNT_TABLE_NAME + "." + UserDAOImpl.ID_COLUMN_NAME +
                    " = " +
                        LINK_TABLE_NAME + "." + LINK_ACCOUNT_ID_COLUMN_NAME +
                " WHERE " +
                        LINK_TABLE_NAME + "." + LINK_CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        LINK_PERMISSION_LEVEL_COLUMN_NAME + " = ?;";

        List<ChatSessionUser> chatSessionUserList = null;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {

            preparedStatement.setInt(1, sessionId);
            preparedStatement.setString(
                    2,
                    chatPermissionLevel.getDatabaseEnumStringValue()
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            chatSessionUserList = generateChatSessionUserList(resultSet);

            connection.close();
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatSessionUserList;
    }

    /**
     * Retrieve
     *
     * @param userId
     * @return Return all ChatSession instances, with all Messages
     */
    @Override
    public List<ChatSession> getChatSessionList(int userId) {
        List<ChatSession> chatSessionList = new ArrayList<>();
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        StringBuilder sqlStatement =
                new StringBuilder(
                        "SELECT *" +
                        " FROM " +
                                CHAT_SESSION_TABLE_NAME +
                        " WHERE " +
                                CHAT_SESSION_ID_COLUMN_NAME +
                        " IN ( "
                );

        List<Integer> chatSessionIdList =
                getChatSessionLinksByUser(connection, userId);

        //Append the Wildcard parameters to the SQL statement
        if (chatSessionIdList.size() > 0) {
            for (int i = 0; i < chatSessionIdList.size(); i++) {
                //Append to the SQL statement
                if (i == chatSessionIdList.size() - 1) {
                    sqlStatement.append(" ?)");
                } else {
                    sqlStatement.append(" ?, ");
                }
            }

            try (
                    PreparedStatement preparedStatement =
                            connection.prepareStatement(sqlStatement.toString())
            ) {
                if (chatSessionIdList.size() > 0) {
                    for (int i = 0; i < chatSessionIdList.size(); i++) {
                        //Store the Session ID as an SQL wildcard
                        preparedStatement.setInt(
                                i + 1,
                                chatSessionIdList.get(i)
                        );
                    }

                    ResultSet resultSet = preparedStatement.executeQuery();

                    chatSessionList = generateChatSessionList(resultSet);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Clean Up Connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return chatSessionList;
    }

    @Override
    public ChatSession getById(int id) {
        ChatSession chatSession = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " + "*" +
                " FROM " +
                        CHAT_SESSION_TABLE_NAME +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?;";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                if (resultSet.next()) {
                    chatSession = generateChatSession(resultSet);
                }
                resultSet.close();
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatSession;
    }

    /**
     * @param resultSet
     * @return
     */
    private List<ChatSession> generateChatSessionList(ResultSet resultSet) {
        List<ChatSession> chatSessionList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                chatSessionList.add(new ChatSession(
                        resultSet.getInt(CHAT_SESSION_ID_COLUMN_NAME),
                        resultSet.getString(CHAT_SESSION_NAME_COLUMN_NAME),
                        resultSet.getObject(
                                CHAT_SESSION_CREATION_TIME_COLUMN_NAME,
                                LocalDateTime.class
                        )
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (chatSessionList.isEmpty()) {
            chatSessionList = null;
        }

        return chatSessionList;
    }

    /**
     * @param resultSet
     * @return
     */
    private ChatSession generateChatSession(ResultSet resultSet) {
        ChatSession chatSession = null;

        try {
            chatSession = new ChatSession(
                    resultSet.getInt(CHAT_SESSION_ID_COLUMN_NAME),
                    resultSet.getString(CHAT_SESSION_NAME_COLUMN_NAME),
                    resultSet.getObject(
                            CHAT_SESSION_CREATION_TIME_COLUMN_NAME,
                            LocalDateTime.class
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatSession;
    }

    private ChatSessionUser generateChatSessionUser(
            User user,
            int sessionId,
            ChatPermissionLevel permissionLevel
    ) {
        return new ChatSessionUser(user, sessionId, permissionLevel);
    }

    private List<ChatSessionUser> generateChatSessionUserList(ResultSet resultSet) {
        List<ChatSessionUser> userList = new ArrayList<>();
        UserDAOImpl userDAO = new UserDAOImpl();

        try {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt(UserDAOImpl.ID_COLUMN_NAME),
                        resultSet.getString(UserDAOImpl.EMAIL_COLUMN_NAME),
                        resultSet.getString(UserDAOImpl.USERNAME_COLUMN_NAME),
                        userDAO.parseUserStatusFromString(
                                resultSet.getString(UserDAOImpl.STATUS_COLUMN_NAME)
                        ),
                        resultSet.getObject(
                                UserDAOImpl.CREATION_TIME_COLUMN_NAME,
                                LocalDateTime.class
                        )
                );

                userList.add(new ChatSessionUser(
                        user,
                        resultSet.getInt(LINK_CHAT_SESSION_ID_COLUMN_NAME),
                        parsePermissionLevelFromString(
                                resultSet.getString(LINK_PERMISSION_LEVEL_COLUMN_NAME)
                        )
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * Returns the ID of Chat Sessions that a User is a part of
     *
     * @param userId
     * @return
     */
    private List<Integer> getChatSessionLinksByUser(
            Connection connection,
            int userId
    ) {
        String sqlStatement =
                "SELECT " +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME +
                " FROM " +
                        LINK_TABLE_NAME +
                " WHERE " +
                        LINK_ACCOUNT_ID_COLUMN_NAME + "= ?;";
        List<Integer> chatSessionIdList = null;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                chatSessionIdList = new ArrayList<>();

                while (resultSet.next()) {
                    chatSessionIdList.add(resultSet.getInt("chat_session_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatSessionIdList;
    }

    private ChatPermissionLevel parsePermissionLevelFromString(String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
            for (ChatPermissionLevel permissionLevel : ChatPermissionLevel.values()) {
                if (permissionLevel.getDatabaseEnumStringValue().equals(stringValue)) {
                    return permissionLevel;
                }
            }
        }

        return ChatPermissionLevel.NULL;
    }

    private void addUserToSession(
            Connection connection,
            int sessionId,
            int userId
    ) {
        String sqlStatement =
                "INSERT INTO " +
                    LINK_TABLE_NAME + " (" +
                        LINK_CHAT_SESSION_ID_COLUMN_NAME + ", " +
                        LINK_ACCOUNT_ID_COLUMN_NAME +
                    ")" +
                " VALUES (?, ?);";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, userId);

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRowsByCondition(
            Connection connection,
            String tableName,
            String conditionColumnName,
            int conditionValue
    ) {
        String sqlStatement =
                "DELETE *" +
                " FROM " +
                        tableName +
                " WHERE " +
                        conditionColumnName + " = ?;";

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, conditionValue);

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildSelectByIdSqlRequest(String tableName, String... columnNames) {
        String idColumnName = null;
        StringBuilder sqlStatementBuilder = null;
        String sqlStatement = null;

        switch (tableName) {
            case CHAT_SESSION_TABLE_NAME:
                idColumnName = CHAT_SESSION_ID_COLUMN_NAME;
                break;
            case LINK_TABLE_NAME:
                idColumnName = LINK_ID_COLUMN_NAME;
                break;
        }

        if (idColumnName != null) {
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
                            tableName +
                            " WHERE " +
                            idColumnName + " = ?;"
            );

            sqlStatement = sqlStatementBuilder.toString();
        }

        return sqlStatement;
    }
}