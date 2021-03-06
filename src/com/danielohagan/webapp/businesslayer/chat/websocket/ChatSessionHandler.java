package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.AttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.json.ChatMessageJsonDecoder;
import com.danielohagan.webapp.businesslayer.chat.websocket.json.ChatMessageJsonEncoder;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
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
    private UserDAOImpl mUserDAO;
    private ChatMessageJsonEncoder mChatMessageJsonEncoder;
    private ChatMessageJsonDecoder mChatMessageJsonDecoder;

    public ChatSessionHandler(int chatSessionId) {
        CHAT_SESSION_ID = chatSessionId;
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
        mUserDAO = new UserDAOImpl();
        mSessionList = new ArrayList<>();
        mChatMessageJsonEncoder = new ChatMessageJsonEncoder();
        mChatMessageJsonDecoder = new ChatMessageJsonDecoder();

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
        mUserDAO = new UserDAOImpl();
        mSessionList = new ArrayList<>();
        mChatMessageJsonEncoder = new ChatMessageJsonEncoder();
        mChatMessageJsonDecoder = new ChatMessageJsonDecoder();

        mChatSession = chatSession;
    }

    public void createChatSession(Integer creatorId, Session session) {
        //Store in database
        mChatSessionDAO.createNewSession(mChatSession, creatorId);

        //Add user list
        mChatSession.getUserList().addAll(
                mChatSessionDAO.getUserListBySession(CHAT_SESSION_ID)
        );

        //Add Chat Session to client's Chat Session list
        sendAddNewChatSession(session);

        //Display the Chat Session
        sendDisplayChatSession(session);
    }

    public void addSession(Session session) {
        if (!mSessionList.contains(session)) {
            mSessionList.add(session);

            sendDisplayChatSession(session);
        }
    }

    public void addUser(String message) {
        Integer userId = mChatMessageJsonDecoder.getUserId(message);
        Integer newUserId = mChatMessageJsonDecoder.getTargetUserId(message);

        ErrorResponse errorResponse = addUserGetErrors(userId, newUserId);

        if (!errorResponse.hasError()) {
            //Add to DB
            mChatSessionDAO.addUserToSession(
                    CHAT_SESSION_ID,
                    newUserId,
                    ChatPermissionLevel.MEMBER
            );

            ChatSessionUser chatSessionUser = new ChatSessionUser(
                    mUserDAO.getById(newUserId),
                    CHAT_SESSION_ID,
                    ChatPermissionLevel.MEMBER
            );

            //Add to memory
            mChatSession.getUserList().add(chatSessionUser);

            //Tell clients to display new User
            sendToAllSessions(
                    mChatMessageJsonEncoder.generateDisplayUserJson(chatSessionUser)
            );
        } else {
            //Send Error response
        }
    }

    public void removeUser(String message) {
        Integer userId = mChatMessageJsonDecoder.getUserId(message);
        Integer targetUserId = mChatMessageJsonDecoder.getTargetUserId(message);

        ErrorResponse errorResponse = removeUserGetErrors(userId, targetUserId);

        if (!errorResponse.hasError()) {
            //Remove Link from Database
            mChatSessionDAO.removeUserFromSession(CHAT_SESSION_ID, targetUserId);

            //Send remove message to session
            sendToAllSessions(
                    mChatMessageJsonEncoder.generateRemoveUserFromSession(
                            CHAT_SESSION_ID,
                            targetUserId
                    )
            );
        } else {
            //Send Error response
        }
    }

    public void updateUserPermission(String message) {
        //TODO:: This

        //TODO:: Update ChatSession.mUserList item
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
            JsonObject removeMessage = mChatMessageJsonEncoder.generateDeleteMessage(id);
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
        //TODO:: Re-write this method AND ADD ERROR CHECKS

        //JSON Message Validation
        if (isValidJsonMessage(jsonMessage)) {
            //Deserialise
            String body;
            Integer userId;

            try (JsonReader reader = Json.createReader(new StringReader(jsonMessage))) {
                JsonObject jsonObject = reader.readObject();

                body = jsonObject.getString(
                        AttributeEnum.BODY.toString()
                );
                userId = mChatMessageJsonDecoder.getUserId(jsonMessage);
            }

            if (body != null && userId != null) {
                Message message = new Message();

                int id = Random.generateRandomPositiveInt();

                while (mMessageDAO.exists(id)) {
                    id = Random.generateRandomPositiveInt();
                }

                message.setBody(body);
                message.setSenderId(userId);
                message.setChatSessionId(CHAT_SESSION_ID);
                message.setId(id);
                message.setCreationTime(LocalDateTime.now());

                //Store in database
                mMessageDAO.createNewMessage(message);

                //Add to List
                mChatSession.getMessageList().add(message);

                //Send as JSON to web socket sessions
                sendToAllSessions(mChatMessageJsonEncoder.generateDisplayMessageJson(
                        message,
                        true
                ));
            }
        }
    }

    public void deleteMessage(String message) {
        Integer userId = mChatMessageJsonDecoder.getUserId(message);
        Integer messageId = mChatMessageJsonDecoder.getMessageId(message);

        ErrorResponse errorResponse = deleteMessageGetErrors(userId, messageId);

        if (!errorResponse.hasError()) {
            mMessageDAO.deleteMessage(messageId);

            mChatSession.removeMessageById(messageId);

            //Send remove message to clients
            sendToAllSessions(mChatMessageJsonEncoder.generateDeleteMessage(messageId));
        } else {
            //Send Error response
        }
    }

    public void deleteChatSession(String message, Session session) {
        Integer userId = mChatMessageJsonDecoder.getUserId(message);

        ErrorResponse errorResponse = deleteChatSessionGetErrors(userId);
        if (!errorResponse.hasError()) {
            //Remove from DB
            mChatSessionDAO.deleteSession(CHAT_SESSION_ID);

            //Tell clients to remove from display
            sendDeleteChatSession();

            cleanUp();
        } else {
            //Send Error response
            sendErrorResponse(session, errorResponse);
        }
    }

    private void sendDeleteChatSession() {
        //Tell Sessions to remove chat Session
        sendToAllSessions(
                mChatMessageJsonEncoder.generateRemoveChatSessionFromDisplay(
                        CHAT_SESSION_ID
                )
        );
    }

    private void sendAddNewChatSession(Session session) {
        //Send message to client instructing to add
        //new Chat Session to display list
        JsonObject addNewChatSessionJson =
                mChatMessageJsonEncoder.generateAddChatSession(
                        mChatSession.getId(),
                        mChatSession.getName()
                );

        sendToSession(session, addNewChatSessionJson);
    }

    public void editMessage(String message, Session session) {
        //TODO:: Re-write this method
        Integer messageId = mChatMessageJsonDecoder.getMessageId(message);
        Integer userId = mChatMessageJsonDecoder.getUserId(message);
        String newMessageBody = null;
        ChatPermissionLevel permissionLevel;

        //TODO: This

        if (messageId != null && userId != null) {
            permissionLevel =
                    mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, userId);

            //Check User has permission
            if (
                    permissionLevel != ChatPermissionLevel.OBSERVER &&
                    permissionLevel != ChatPermissionLevel.NULL
            ) {
                newMessageBody = mChatMessageJsonDecoder.getMessageBody(message);

                if (newMessageBody != null) {
                    //Change in database
                    mMessageDAO.updateMessageBody(messageId, newMessageBody);

                    //Change in mMessageList
                    mChatSession.getMessageById(messageId).setBody(newMessageBody);

                    //Send update message to clients
                    sendDisplayUpdateMessageBody(messageId, newMessageBody);
                }

            } else {
                sendInfo(session, ChatErrorType.PERMISSION_CHECK_FAILED_UPDATE_MESSAGE);
            }
        }
    }

    //Remove this method when re-writing addNewMessage method
    @Deprecated
    private boolean isValidJsonMessage(String jsonMessage) {
        //TODO:: Add to this

        if (jsonMessage == null) {
            return false;
        } else if (jsonMessage.length() <= 0) {
            return false;
        }

        return true;
    }

    public void sendErrorResponse(Session session, ErrorResponse errorResponse) {
        //TODO:: THIS
    }

    public void sendError(Session session, IErrorType error) {
        //TODO:: THIS
    }

    public void sendInfo(Session session, IErrorType info) {
        //TODO:: THIS
    }

    public void sendDisplayChatSession(Session session) {
        //Tell session to display chat session specifics
        sendChatSessionConfig(session);

        //Update name
        sendDisplayChatSessionName(session);

        //Tell session to display existing messages
        sendDisplayMessagesToSession(mChatSession.getMessageList(), session, true);

        //Tell session to display existing members
        sendDisplayUsersToSession(mChatSession.getUserList(), session);
    }

    private void sendChatSessionConfig(Session session) {
        //TODO:: Send chat session specific configurations to session

    }

    private void sendDisplayChatSessionName(Session session) {
        JsonObject displayName =
                mChatMessageJsonEncoder.generateChatSessionName(mChatSession.getName());

        sendToSession(session, displayName);
    }

    private void sendDisplayUpdateMessageBody(int messageId, String messageBody) {
        sendToAllSessions(
                mChatMessageJsonEncoder.generateDisplayUpdateMessage(
                        messageId,
                        messageBody
                )
        );
    }

    private void sendDisplayMessagesToSession(
            List<Message> messageList,
            Session session,
            boolean isNewMessages
    ) {
        if (messageList != null && messageList.size() > 0) {
            for (Message message : messageList) {
                JsonObject displayMessageJson =
                        mChatMessageJsonEncoder.generateDisplayMessageJson(message, isNewMessages);

                sendToSession(session, displayMessageJson);
            }
        }
    }

    private void sendDisplayUsersToSession(List<ChatSessionUser> userList, Session session) {
        if (userList != null && userList.size() > 0) {
            for (ChatSessionUser user : userList) {
                JsonObject displayMessageJson = mChatMessageJsonEncoder.generateDisplayUserJson(user);
                sendToSession(session, displayMessageJson);
            }
        }
    }

    private ErrorResponse addUserGetErrors(
            Integer userId,
            Integer newUserId
    ) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null || newUserId == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If Chat Session exists
            if (!mChatSessionDAO.exists(CHAT_SESSION_ID)) {
                errorResponse.add(ChatErrorType.SESSION_DOES_NOT_EXIST);
            } else {
                //If User exists
                if (!mUserDAO.exists(userId)) {
                    errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
                }

                //If New User exists
                if (!mUserDAO.exists(newUserId)) {
                    errorResponse.add(ChatErrorType.NEW_USER_NOT_FOUND);
                }

                //If User is in Chat Session
                if (!mChatSessionDAO.userIsInChatSession(userId, CHAT_SESSION_ID)) {
                    errorResponse.add(ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION);
                }

                //If New User is in Chat Session
                if (mChatSessionDAO.userIsInChatSession(newUserId, CHAT_SESSION_ID)) {
                    errorResponse.add(ChatErrorType.NEW_USER_ALREADY_IN_SESSION);
                }

                //If has permission
                ChatPermissionLevel permissionLevel =
                        mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, userId);

                if (!ServerActionEnum.PermissionLists.hasPermission(
                        ServerActionEnum.ADD_USER,
                        permissionLevel
                )) {
                    errorResponse.add(ChatErrorType.PERMISSION_CHECK_FAILED_ADD_USER);
                }
            }
        }

        return errorResponse;
    }

    private ErrorResponse removeUserGetErrors(
            Integer userId,
            Integer targetUserId
    ) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null ||  targetUserId == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If User exists
            if (!mUserDAO.exists(userId)) {
                errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
            } else {

                //If Target User exists
                if (!mUserDAO.exists(targetUserId)) {
                    errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
                }

                //If User is in Chat Session
                if (!mChatSessionDAO.userIsInChatSession(userId, CHAT_SESSION_ID)) {
                    errorResponse.add(ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION);
                }

                //If Target User is not in Chat Session
                if (!mChatSessionDAO.userIsInChatSession(targetUserId, CHAT_SESSION_ID)) {
                    errorResponse.add(ChatErrorType.USER_NOT_IN_SESSION);
                }

                //If User has permission
                ChatPermissionLevel permissionLevel =
                        mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, userId);

                if (
                    //If User is removing oneself
                    !userId.equals(targetUserId) &&

                    //If User has Remove User permission
                    !ServerActionEnum.PermissionLists.hasPermission(
                            ServerActionEnum.REMOVE_USER, permissionLevel
                    )
                ) {
                    errorResponse.add(ChatErrorType.PERMISSION_CHECK_FAILED_REMOVE_USER);
                }

                permissionLevel = mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, targetUserId);
                if (
                    //If target user can be removed
                        permissionLevel == ChatPermissionLevel.CREATOR
                ) {
                    errorResponse.add(ChatErrorType.CANT_REMOVE_CREATOR);
                }
            }
        }

        return errorResponse;
    }

    private ErrorResponse addNewMessageGetErrors(
            Integer userId,
            String body
    ) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null || body == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If User exists
            if (!mUserDAO.exists(userId)) {
                errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
            } else {
                //If Chat Session exists
                if (!mChatSessionDAO.exists(CHAT_SESSION_ID)) {
                    errorResponse.add(ChatErrorType.SESSION_DOES_NOT_EXIST);
                } else {
                    //If User is in Chat Session
                    if (!mChatSessionDAO.userIsInChatSession(userId, CHAT_SESSION_ID)) {
                        errorResponse.add(ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION);
                    }
                }
            }
        }

        return errorResponse;
    }

    private ErrorResponse deleteMessageGetErrors(
            Integer userId,
            Integer messageId
    ) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null || messageId == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If User exists
            if (!mUserDAO.exists(userId)) {
                errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
            }

            //If User is in Chat Session
            if (!mChatSessionDAO.userIsInChatSession(userId, CHAT_SESSION_ID)) {
                errorResponse.add(ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION);
            } else {
                //If Message exists
                if (!mMessageDAO.exists(messageId)) {
                    errorResponse.add(ChatErrorType.MESSAGE_DOES_NOT_EXIST);
                } else {
                    if (
                        //If User is not message sender
                        !userId.equals(mMessageDAO.getMessageSenderId(messageId)) &&

                        //If User does not have permission
                        !ServerActionEnum.PermissionLists.hasPermission(
                                ServerActionEnum.DELETE_MESSAGE,
                                mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, userId)
                        )
                    ) {
                        errorResponse.add(ChatErrorType.PERMISSION_CHECK_FAILED_DELETE_MESSAGE);
                    }
                }
            }
        }

        return errorResponse;
    }

    private ErrorResponse deleteChatSessionGetErrors(Integer userId) {
        ErrorResponse errorResponse = new ErrorResponse();

        if (userId == null) {
            errorResponse.add(ChatErrorType.UNABLE_TO_RETRIEVE_REQUIRED_ID);
        } else {
            //If User exists
            if (!mUserDAO.exists(userId)) {
                errorResponse.add(ChatErrorType.USER_ID_NOT_FOUND);
            }

            //If has permission
            if (!ServerActionEnum.PermissionLists.hasPermission(
                    ServerActionEnum.DELETE_CHAT_SESSION,
                    mChatSessionDAO.getUserPermissionLevel(CHAT_SESSION_ID, userId)
            )) {
                errorResponse.add(ChatErrorType.PERMISSION_CHECK_FAILED_DELETE_CHAT_SESSION);
            }
        }

        return errorResponse;
    }

    public void sendSetChatSessionId(Session session) {
        sendToSession(
                session,
                mChatMessageJsonEncoder.generateSetChatSessionId(CHAT_SESSION_ID)
        );
    }

    public int getChatSessionId() {
        return CHAT_SESSION_ID;
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

        if (mChatSession != null) {
            mChatSession.cleanUp();
        }
    }
}