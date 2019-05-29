package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.chat.Message;

import java.util.List;

public interface IMessageDAO extends IEntityDAO<Message> {

    void createNewMessage(Message message);

    void deleteMessage(int sessionId, int messageId);
    void deleteMessageByUser(int sessionId, int userId);

    List<Message> getChatSessionMessageList(int sessionId);
    List<Message> getMessageListByUser(int sessionId, int userId);
}