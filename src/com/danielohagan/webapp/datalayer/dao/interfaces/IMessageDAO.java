package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.chat.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IMessageDAO extends IEntityDAO<Message> {

    void createNewMessage(Message message);

    void updateMessageBody(int messageId, String body);

    void deleteMessage(int messageId);
    void deleteMessageByUser(int userId);
    void deleteSessionMessageByUser(int sessionId, int userId);
    void deleteMessageBySession(int sessionId);

    List<Message> getChatSessionMessageList(int sessionId);
    List<Message> getChatSessionMessageListByUser(int sessionId, int userId);
    List<Message> getChatSessionMessageListBeforeTime(int sessionId, LocalDateTime time);
    List<Message> getChatSessionMessageListBeforeTime(int sessionId, LocalDateTime time, int messageCount);

    Map<String, String> getColumnStringsById(int id, String... columnNames);
    Map<String, Integer> getColumnIntegersById(int id, String... columnNames);

}