package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;

import java.util.List;

public interface IChatSessionDAO extends IEntityDAO<ChatSession> {

    void createNewSession(ChatSession chatSession);
    void deleteSession(int sessionId);

    void updateSessionName(int sessionId, String name);

    void addUser(int sessionId, int userId);
    void removeUser(int sessionId, int userId);

    void setUserPermissionLevel(int sessionId, int userId, ChatPermissionLevel chatPermissionLevel);
    ChatPermissionLevel getUserPermissionLevel(int sessionId, int userId);

    boolean exists(int sessionId);
    boolean userIsInChatSession(int userId, int sessionId);

    List<ChatSessionUser> getUserListBySession(int sessionId);
    List<ChatSessionUser> getUserListByPermission(int sessionId, ChatPermissionLevel chatPermissionLevel);
    List<ChatSession> getChatSessionList(int userId);

}