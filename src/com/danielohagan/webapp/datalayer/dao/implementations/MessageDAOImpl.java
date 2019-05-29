package com.danielohagan.webapp.datalayer.dao.implementations;

import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.interfaces.IMessageDAO;
import com.danielohagan.webapp.datalayer.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements IMessageDAO {

    @Override
    public Message getById(int id) {
        return null;
    }

    @Override
    public void createNewMessage(Message message) {

    }

    @Override
    public void deleteMessage(int sessionId, int messageId) {

    }

    /**
     * Delete every message sent by a User in a Chat Session
     *
     * @param sessionId The target Chat Session ID
     * @param userId The target User ID
     */
    @Override
    public void deleteMessageByUser(int sessionId, int userId) {

    }

    @Override
    public List<Message> getChatSessionMessageList(int sessionId) {
        Connection connection =
                DatabaseConnection.getDatabaseConnection();

        return getMessageList(connection, sessionId);
    }

    @Override
    public List<Message> getMessageListByUser(int sessionId, int userId) {
        return null;
    }

    /**
     *
     * @param connection
     * @param sessionId
     *
     * @return
     */
    private List<Message> getMessageList(Connection connection, int sessionId) {
        List<Message> messageList = null;
        String sqlStatement =
                "SELECT *" +
                        " FROM " + "message_table" +
                        " WHERE " + "message_chat_session_id" + "= ?";

        if (connection != null) {
            try (
                    PreparedStatement preparedStatement =
                            connection.prepareStatement(sqlStatement)
            ) {

                preparedStatement.setInt(1, sessionId);

                ResultSet resultSet = preparedStatement.executeQuery();

                messageList = generateMessageList(resultSet);

                //Clean Up
                preparedStatement.close();
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return messageList;
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
                messageList.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("message_sender_id"),
                        resultSet.getInt("message_chat_session_id"),
                        resultSet.getString("message_body"),
                        resultSet.getObject("message_creation_time", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (messageList.isEmpty()) {
            messageList = null;
        }

        return messageList;
    }
}