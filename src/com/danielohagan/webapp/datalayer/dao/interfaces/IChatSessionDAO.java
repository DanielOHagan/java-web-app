package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;

import java.util.List;
import java.util.Map;

public interface IChatSessionDAO extends IEntityDAO<ChatSession> {

    void createNewSession(ChatSession chatSession, int creatorId);
    void deleteSession(int sessionId);

    void updateSessionName(int sessionId, String name);

    void addUserToSession(int sessionId, int userId);
    void removeUserFromSession(int sessionId, int userId);

    void removeLinksByUser(int userId);
    void removeLinksBySession(int sessionId);

    void updateUserPermissionLevel(int sessionId, int userId, ChatPermissionLevel chatPermissionLevel);
    ChatPermissionLevel getUserPermissionLevel(int sessionId, int userId);

    boolean userIsInChatSession(int userId, int sessionId);

    List<ChatSessionUser> getUserListBySession(int sessionId);
    List<ChatSessionUser> getUserListByPermission(int sessionId, ChatPermissionLevel chatPermissionLevel);
    List<ChatSession> getChatSessionList(int userId);

    Map<String, String> getColumnStringsById(int id, String tableName, String... columnNames);
    Map<String, Integer> getColumnIntegersById(int id, String tableName, String... columnNames);

}