package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.AttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.json.ChatJsonBuilder;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.ChatErrorType;
import com.danielohagan.webapp.error.type.IErrorType;
import com.danielohagan.webapp.utils.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.Session;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatSessionHandler {

    /*
    Each ChatSessionHandler handles the request for a single ChatSession
     */

    private final int DEFAULT_MESSAGE_COUNT = 20; //Number of messages to load by default
    private final int CHAT_SESSION_ID;
    private final ChatSession mChatSession;

    private List<Session> mSessionList; //List of the WebSocket Clients
    private ChatSessionDAOImpl mChatSessionDAO;
    private MessageDAOImpl mMessageDAO;
    private ChatJsonBuilder mChatJsonBuilder;

    public ChatSessionHandler(int chatSessionId) {
        CHAT_SESSION_ID = chatSessionId;
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
        mSessionList = new ArrayList<>();
        mChatJsonBuilder = new ChatJsonBuilder();

        //Build the Chat Session
        mChatSession = mChatSessionDAO.getById(CHAT_SESSION_ID);
        //Set the message list to the latest
        //"DEFAULT_MESSAGE_COUNT" number of messages sent
        mChatSession.setMessageList(mMessageDAO.getChatSessionMessageListBeforeTime(
                CHAT_SESSION_ID,
                LocalDateTime.now(),
                DEFAULT_MESSAGE_COUNT
        ));
        mChatSession.setUserList(mChatSessionDAO.getUserListBySession(chatSessionId));
    }

    public ChatSessionHandler(ChatSession chatSession) {
        CHAT_SESSION_ID = chatSession.getId();
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
        mSessionList = new ArrayList<>();
        mChatJsonBuilder = new ChatJsonBuilder();

        mChatSession = chatSession;
    }

    public void createChatSession(Integer creatorId, Session session) {
        //Store in database
        mChatSessionDAO.createNewSession(mChatSession, creatorId);

        //Add Chat Session to client's chat session list
        sendDisplayChatSession(session);
    }

    public void addSession(Session session) {
        if (!mSessionList.contains(session)) {
            mSessionList.add(session);

            sendDisplayChatSession(session);
        }
    }

    public void addUser(String message) {
        Integer userId = WebSocketMessageUtils.getUserId(message);
        Integer sessionId = WebSocketMessageUtils.getChatSessionId(message);
        Integer newUserId = WebSocketMessageUtils.getNewUserId(message);

        //TODO:: This

        ErrorResponse errorResponse = addUserGetErrors(userId, sessionId, newUserId);

        if (errorResponse.highestSeverity() != ErrorSeverity.INFO) {
            mChatSessionDAO.addUserToSession(
                    sessionId,
                    newUserId,
                    ChatPermissionLevel.MEMBER
            );

            ChatSessionUser chatSessionUser = new ChatSessionUser(
                    new UserDAOImpl().getById(userId),
                    sessionId,
                    mChatSessionDAO.getUserPermissionLevel(userId, sessionId)
            );

            sendToAllSessions(mChatJsonBuilder.generateDisplayUserJson(chatSessionUser));
        }
    }

    public void removeUser(String message) {

    }

    public void updateUserPermission(String message) {

    }

    /**
     * Remove a WebSocket Client session
     *
     * @param session WebSocket Client to remove
     */
    public void removeSession(Session session) {
        mSessionList.remove(session);
    }

    public void sendDeleteMessage(int id) {
        Message message = getMessage(id);

        if (message != null) {
            mChatSession.getMessageList().remove(message);
            JsonObject removeMessage = mChatJsonBuilder.generateDeleteMessage(id);
            sendToAllSessions(removeMessage);
        }
    }

    public void displayOlderMessags(
            int sessionId,
            LocalDateTime oldestCreationDate,
            Session session
    ) {
        List<Message> olderMessages = mMessageDAO.getChatSessionMessageListBeforeTime(
                sessionId,
                oldestCreationDate,
                DEFAULT_MESSAGE_COUNT
        );

        //Add older message if any exist
        if (olderMessages != null && olderMessages.size() > 0) {
            //TODO:: Check if the order is right
            mChatSession.getMessageList().addAll(olderMessages);

            //Send display message to session
            sendDisplayMessagesToSession(olderMessages, session, false);
        }
    }

    public Message getMessage(int id) {
        for (Message message : mChatSession.getMessageList()) {
            if (message != null && message.getId() == id) {
                return message;
            }
        }

        return null;
    }

    public void sendToAllSessions(JsonObject jsonObject) {
        for (Session session : mSessionList) {
            sendToSession(session, jsonObject);
        }
    }

    public void sendToSession(Session session, JsonObject jsonObject) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(jsonObject.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewMessage(String jsonMessage) {
        Message message;

        //JSON Message Validation
        if (isValidJsonMessage(jsonMessage)) {
            //Deserialise
            String body;
            Integer userId;

            try (JsonReader reader = Json.createReader(new StringReader(jsonMessage))) {
                JsonObject jsonObject = reader.readObject();

                body = jsonObject.getString(
                        AttributeEnum.BODY.getAttributeString()
                );
                userId = WebSocketMessageUtils.getUserId(jsonMessage);
            }

            if (body != null && userId != null) {
                message = new Message();

                int id = Random.generateRandomPositiveInt();

                while (mMessageDAO.exists(id)) {
                    id = Random.generateRandomPositiveInt();
                }

                message.setBody(body);
                message.setSenderId(userId);
                message.setChatSessionId(CHAT_SESSION_ID);
                message.setId(id);
                message.setCreationTime(LocalDateTime.now());

                //TODO: Validation checks
                //    With error handling
                //Message Entity validation


                //Store in database
                mMessageDAO.createNewMessage(message);

                //Send as JSON to web socket sessions
                sendToAllSessions(mChatJsonBuilder.generateDisplayMessageJson(
                        message,
                        true
                ));
            }
        }
    }

    public void deleteMessage(String message) {
        //Check User has permission

        //TODO:: Remove from database

        //TODO:: Remove from mMessageList

        //Send remove message to clients
    }

    public void deleteChatSession(String message, Session session) {
        Integer sessionId = WebSocketMessageUtils.getChatSessionId(message);
        Integer userId = WebSocketMessageUtils.getUserId(message);

        if (
                sessionId != null &&
                userId != null &&
                mChatSessionDAO.getUserPermissionLevel(sessionId, userId)
                        == ChatPermissionLevel.CREATOR
        ) {
            mChatSessionDAO.deleteSession(sessionId);
            sendDeleteChatSession(sessionId);
            cleanUp();
        } else {
            sendError(
                    session,
                    ChatErrorType.PERMISSION_CHECK_FAILED_SESSION_DELETION
            );
        }
    }

    private void sendDeleteChatSession(Integer sessionId) {
        //Tell Sessions to remove chat Session

        //TODO::

//        JsonObject removeChatSessionJson = mChatJsonBuilder.generateRemoveChatSessionJson(sessionId);
//        sendToAllSessions(removeChatSessionJson);
    }

    public void editMessage(String message, Session session) {
        Integer messageId = WebSocketMessageUtils.getMessageId(message);
        Integer userId = WebSocketMessageUtils.getUserId(message);
        Integer sessionId = WebSocketMessageUtils.getChatSessionId(message);

        //TODO: This

        //Check User has permission
//        if (has permission) {
//            //Change in database
//
//            //Change in mMessageList
//            mChatSession.getMessageById(messageId).setBody();
//
//            //Send update message to clients
//
//        } else {
//            sendInfo(session, ChatErrorType.PERMISSION_CHECK_FAILED_UPDATE_MESSAGE);
//        }
    }

    private boolean isValidJsonMessage(String jsonMessage) {
        //TODO:: Add to this

        if (jsonMessage == null) {
            return false;
        } else if (jsonMessage.length() <= 0) {
            return false;
        }

        return true;
    }

    public void sendError(Session session, IErrorType error) {
        //TODO:: THIS
    }

    public void sendInfo(Session session, IErrorType info) {
        //TODO:: THIS
    }

    public void sendDisplayChatSession(Session session) {
        //Tell session to change to current Chat Session
        //TODO: Maybe have JS clear the old session display instead of sending a message from
        // the server telling the client to do so anyway

        //Tell session to display chat session specifics
        sendChatSessionConfig(session);

        sendDisplayChatSessionName(session);

        //Tell session to display existing messages
        sendDisplayMessagesToSession(mChatSession.getMessageList(), session, true);

        //Tell session to display existing members
        sendDisplayUsersToSession(mChatSession.getUserList(), session);
    }

    private void sendChatSessionConfig(Session session) {
        //TODO::

    }

    private void sendDisplayChatSessionName(Session session) {
        JsonObject displayName =
                mChatJsonBuilder.generateChatSessionName(mChatSession.getName());

        sendToSession(session, displayName);
    }

    private void sendDisplayMessagesToSession(
            List<Message> messageList,
            Session session,
            boolean isNewMessages
    ) {
        if (messageList != null && messageList.size() > 0) {
            for (Message message : messageList) {
                JsonObject displayMessageJson =
                        mChatJsonBuilder.generateDisplayMessageJson(message, isNewMessages);

                sendToSession(session, displayMessageJson);
            }
        }
    }

    private void sendDisplayUsersToSession(List<ChatSessionUser> userList, Session session) {
        if (userList != null && userList.size() > 0) {
            for (ChatSessionUser user : userList) {
                JsonObject displayMessageJson = mChatJsonBuilder.generateDisplayUserJson(user);
                sendToSession(session, displayMessageJson);
            }
        }
    }

    private ErrorResponse addUserGetErrors(
            Integer userId,
            Integer sessionId,
            Integer newUserId
    ) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null || sessionId == null || newUserId == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If is in Chat Session
            if (!mChatSessionDAO.userIsInChatSession(userId, sessionId)) {
                errorResponse.add(ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION);
            }

            //If has permission
            if (
                    mChatSessionDAO.getUserPermissionLevel(sessionId, userId)
                            == ChatPermissionLevel.ADMIN ||
                    mChatSessionDAO.getUserPermissionLevel(sessionId, userId)
                            == ChatPermissionLevel.CREATOR
            ) {
                errorResponse.add(ChatErrorType.PERMISSION_CHECK_FAILED_ADD_USER);
            }
        }

        return errorResponse;
    }

    public void cleanUp() {
        if (mSessionList != null) {
            mSessionList.clear();
            mSessionList = null;
        }

        if (mMessageDAO != null) {
            mMessageDAO = null;
        }

        if (mChatSessionDAO != null) {
            mChatSessionDAO = null;
        }
    }
}