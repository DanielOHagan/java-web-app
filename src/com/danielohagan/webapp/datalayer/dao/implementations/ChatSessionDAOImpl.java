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
import java.util.List;

public class ChatSessionDAOImpl implements IChatSessionDAO {



    //TODO:: Environment variables


    @Override
    public void createNewSession(ChatSession chatSession) {

    }

    @Override
    public void deleteSession(int sessionId) {

    }

    @Override
    public void updateSessionName(int sessionId, String name) {

    }

    /**
     * Add a User to a Chat Session, assuming that the User is not already in the Chat Session
     *
     * @param sessionId Target Chat Session to add User
     * @param userId Target User to add to Chat Session
     */
    @Override
    public void addUser(int sessionId, int userId) {

    }

    /**
     * Removes a user from the Chat Session
     *
     * @param sessionId ID of the target Chat Session
     * @param userId ID of the target User to be removed
     */
    @Override
    public void removeUser(int sessionId, int userId) {

    }

    @Override
    public void setUserPermissionLevel(
            int sessionId,
            int userId,
            ChatPermissionLevel chatPermissionLevel
    ) {

    }

    @Override
    public ChatPermissionLevel getUserPermissionLevel(int sessionId, int userId) {
        return null;
    }

    @Override
    public boolean exists(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " + "session_id" +
                        " FROM " + "chat_session_table" +
                        " WHERE " + "session_id" + " = ?;";

        try(
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
    public boolean userIsInChatSession(int userId, int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " + "link_id" +
                        " FROM " + "chat_session_account_link_table" +
                        " WHERE " + "account_id" + " = ?" +
                        " AND " + "chat_session_id" + " = ?;";

        try(
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement)
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, sessionId);

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
    public List<ChatSessionUser> getUserListBySession(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " +
                        " account_table.account_id," +
                        " permission_level," +
                        " account_email," +
                        " account_username," +
                        " account_status," +
                        " account_creation_time," +
                        " chat_session_id" +
                " FROM " +
                        "chat_session_account_link_table" +
                " INNER JOIN " +
                        "account_table" +
                " ON " +
                        "account_table.account_id" +
                        " = " +
                        "chat_session_account_link_table.account_id" +
                " WHERE " +
                        "chat_session_account_link_table.chat_session_id = ?;";

        List<ChatSessionUser> chatSessionUserList = null;

        try(
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
                        " account_table.account_id," +
                        " permission_level," +
                        " account_email," +
                        " account_username," +
                        " account_status," +
                        " account_creation_time," +
                        " chat_session_id" +
                " FROM " +
                        "chat_session_account_link_table" +
                " INNER JOIN " +
                        "account_table" +
                " ON " +
                        "account_table.account_id" +
                        " = " +
                        "chat_session_account_link_table.account_id" +
                " WHERE " +
                        "chat_session_account_link_table.chat_session_id = ?" +
                " AND " +
                        "permission_level = ?;";

        List<ChatSessionUser> chatSessionUserList = null;

        try(
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
     *
     * @return Return all ChatSession instances, with all Messages
     */
    @Override
    public List<ChatSession> getChatSessionList(int userId) {
        List<ChatSession> chatSessionList = new ArrayList<>();
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        StringBuilder sqlStatement =
                new StringBuilder("SELECT *" +
                        " FROM " + "chat_session_table" +
                        " WHERE " + "session_id" + " IN ( ");

        List<Integer> chatSessionIdList =
                getChatSessionLinksByUser(connection, userId);

        //Append the Wildcard parameters to the SQL statement
        if (chatSessionIdList != null && chatSessionIdList.size() > 0) {
            for (int i = 0; i < chatSessionIdList.size(); i++) {
                //Append to the SQL statement
                if (i == chatSessionIdList.size() - 1) {
                    sqlStatement.append(" ?)");
                } else {
                    sqlStatement.append(" ?, ");
                }
            }
        }

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sqlStatement.toString())
        ) {
            if (chatSessionIdList != null && chatSessionIdList.size() > 0) {
                for (int i = 0; i < chatSessionIdList.size(); i++) {
                    //Store the Session ID as an SQL wildcard
                    preparedStatement.setInt(
                            i + 1, // + 1 because PreparedStatement start at 1 instead of 0
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

        return chatSessionList;
    }

    @Override
    public ChatSession getById(int id) {
        ChatSession chatSession = null;
        Connection connection =
                DatabaseConnection.getDatabaseConnection();
        String sqlStatement =
                "SELECT " + "*" +
                        " FROM " + "chat_session_table" +
                        " WHERE " + "session_id" + " = ?;";

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
     *
     * @param resultSet
     * @return
     */
    private List<ChatSession> generateChatSessionList(ResultSet resultSet) {
        List<ChatSession> chatSessionList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                chatSessionList.add(new ChatSession(
                        resultSet.getInt("session_id"),
                        resultSet.getString("session_name"),
                        resultSet.getObject("creation_time", LocalDateTime.class)
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
     *
     * @param resultSet
     * @return
     */
    private ChatSession generateChatSession(ResultSet resultSet) {
        ChatSession chatSession = null;

        try {
            chatSession = new ChatSession(
                    resultSet.getInt("session_id"),
                    resultSet.getString("session_name"),
                    resultSet.getObject("creation_time", LocalDateTime.class)
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
                        resultSet.getObject(UserDAOImpl.CREATION_TIME_COLUMN_NAME, LocalDateTime.class)
                );

                userList.add(new ChatSessionUser(
                        user,
                        resultSet.getInt("chat_session_id"),
                        parsePermissionLevelFromString(
                                resultSet.getString("permission_level")
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
     *
     * @return
     */
    private List<Integer> getChatSessionLinksByUser(Connection connection, int userId) {
        String sqlStatement =
                "SELECT " + "chat_session_id" +
                        " FROM " + "chat_session_account_link_table" +
                        " WHERE " + "account_id" + "= ?;";
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

        if (chatSessionIdList != null && chatSessionIdList.isEmpty()) {
            chatSessionIdList = null;
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
}