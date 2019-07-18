package com.danielohagan.webapp.datalayer.dao.implementations;

import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.interfaces.IMessageDAO;
import com.danielohagan.webapp.datalayer.database.hikari.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDAOImpl implements IMessageDAO {

    public static final String MESSAGE_TABLE_NAME = "message_table";
    public static final String ID_COLUMN_NAME = "message_id";
    public static final String SENDER_ID_COLUMN_NAME = "message_sender_id";
    public static final String CHAT_SESSION_ID_COLUMN_NAME = "message_chat_session_id";
    public static final String BODY_COLUMN_NAME = "message_body";
    public static final String CREATION_TIME_COLUMN_NAME = "message_creation_time";

    @Override
    public Message getById(int id) {
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";
        Message message = null;

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            message = generateMessage(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public boolean exists(int id) {
        String sqlStatement =
                "SELECT " +
                        ID_COLUMN_NAME +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";
        boolean exists = false;

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            exists = resultSet.next();

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    @Override
    public void createNewMessage(Message message) {
        String sqlStatement =
                "INSERT INTO " +
                    MESSAGE_TABLE_NAME + " (" +
                        ID_COLUMN_NAME + ", " +
                        SENDER_ID_COLUMN_NAME + ", " +
                        CHAT_SESSION_ID_COLUMN_NAME + ", " +
                        BODY_COLUMN_NAME + ", " +
                        CREATION_TIME_COLUMN_NAME +
                    ")" +
                " VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, message.getId());
            preparedStatement.setInt(2, message.getSenderId());
            preparedStatement.setInt(3, message.getChatSessionId());
            preparedStatement.setString(4, message.getBody());
            preparedStatement.setObject(5, message.getCreationTime());

            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMessageBody(int messageId, String body) {
        String sqlStatement =
                "UPDATE " +
                        MESSAGE_TABLE_NAME +
                " SET " +
                        BODY_COLUMN_NAME + " = ?" +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, body);
            preparedStatement.setInt(2, messageId);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a single message
     *
     * @param messageId ID of the message to delete
     */
    @Override
    public void deleteMessage(int messageId) {
        deleteColumnRowsById(ID_COLUMN_NAME, messageId);
    }

    /**
     * Delete every message sent by a User in a Chat Session
     *
     * @param sessionId The target Chat Session ID
     * @param userId The target User ID
     */
    @Override
    public void deleteSessionMessageByUser(int sessionId, int userId) {
        String sqlStatement =
                "DELETE *" +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        SENDER_ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, userId);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessageBySession(int sessionId) {
        deleteColumnRowsById(CHAT_SESSION_ID_COLUMN_NAME, sessionId);
    }

    @Override
    public Integer getMessageSenderId(int messageId) {
        return getColumnInteger(messageId, SENDER_ID_COLUMN_NAME);
    }

    /**
     * Delete every message sent by a User
     *
     * @param userId The target User ID
     */
    @Override
    public void deleteMessageByUser(int userId) {
        deleteColumnRowsById(SENDER_ID_COLUMN_NAME, userId);
    }

    /**
     * Get all of the messages that have been sent to a chat session
     *
     * @param sessionId Chat Session ID
     *
     * @return List of all messages from a Chat Session
     */
    @Override
    public List<Message> getChatSessionMessageList(int sessionId) {
        List<Message> messageList = new ArrayList<>();
        String sqlStatement =
                "SELECT *" +
                        " FROM " +
                        MESSAGE_TABLE_NAME +
                        " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + "= ?";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            messageList = generateMessageList(resultSet);

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    /**
     * Get all of the message a User has sent to a chat session
     *
     * @param sessionId The ID of the Chat session to search
     * @param userId The ID of the User
     *
     * @return List of messages that have been sent to the specified
     *         chat session by the specified user
     */
    @Override
    public List<Message> getChatSessionMessageListByUser(
            int sessionId,
            int userId
    ) {
        List<Message> messageList = new ArrayList<>();
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        SENDER_ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, sessionId);
            preparedStatement.setInt(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            messageList = generateMessageList(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    /**
     * Get all Messages from a Chat Session sent
     *  before a specified time
     *
     * @param sessionId The Chat Session
     * @param time The time
     *
     * @return List of Messages sent to the session before the specified time
     */
    @Override
    public List<Message> getChatSessionMessageListBeforeTime(
            int sessionId,
            LocalDateTime time
    ) {
        List<Message> messageList = new ArrayList<>();
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        CREATION_TIME_COLUMN_NAME + " < ?" +
                " ORDER BY " +
                        "-" + CREATION_TIME_COLUMN_NAME + ";";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, sessionId);
            preparedStatement.setObject(2, time);

            ResultSet resultSet = preparedStatement.executeQuery();
            messageList = generateMessageList(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    /**
     * Get a number of Messages from a Chat Session sent before a specified time
     *
     * @param sessionId
     * @param time
     * @param messageCount
     *
     * @return
     */
    @Override
    public List<Message> getChatSessionMessageListBeforeTime(
            int sessionId,
            LocalDateTime time,
            int messageCount
    ) {
        List<Message> messageList = new ArrayList<>();
        String sqlStatement =
                "SELECT *" +
                " FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        CHAT_SESSION_ID_COLUMN_NAME + " = ?" +
                " AND " +
                        CREATION_TIME_COLUMN_NAME + " < ?" +
                " ORDER BY " +
                        CREATION_TIME_COLUMN_NAME +
                " LIMIT " +
                        " ? " + ";";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, sessionId);
            preparedStatement.setObject(2, time);
            preparedStatement.setInt(3, messageCount);

            ResultSet resultSet = preparedStatement.executeQuery();
            messageList = generateMessageList(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    @Override
    public Map<String, String> getColumnStringsById(
            int id,
            String... columnNames
    ) {
        Map<String, String> columnStringsMap = new HashMap<>();
        String sqlStatement = buildSelectByIdSqlRequest(columnNames);

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

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

            preparedStatement.close();
            resultSet.close();
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

            preparedStatement.close();
            resultSet.close();
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
                        MESSAGE_TABLE_NAME +
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

            preparedStatement.close();
            resultSet.close();
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
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getInt(columnName);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Extract message data from the result set and return it as List<Message> type
     *
     * @param resultSet The result set to extract data from
     *
     * @return List off messages extracted from resultSet
     */
    private List<Message> generateMessageList(ResultSet resultSet) {
        List<Message> messageList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                messageList.add(generateMessage(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    private Message generateMessage(ResultSet resultSet) {
        try {
            return new Message(
                    resultSet.getInt(ID_COLUMN_NAME),
                    resultSet.getInt(SENDER_ID_COLUMN_NAME),
                    resultSet.getInt(CHAT_SESSION_ID_COLUMN_NAME),
                    resultSet.getString(BODY_COLUMN_NAME),
                    resultSet.getObject(
                            CREATION_TIME_COLUMN_NAME,
                            LocalDateTime.class
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void deleteColumnRowsById(String column, int id) {
        String sqlStatement =
                "DELETE FROM " +
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        column + " = ?;";

        try (Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, id);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        MESSAGE_TABLE_NAME +
                " WHERE " +
                        ID_COLUMN_NAME + " = ?;"
        );

        sqlStatement = sqlStatementBuilder.toString();

        return sqlStatement;
    }
}