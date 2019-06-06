package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.EntityAttributeEnum.MessageEntityAttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.EntityAttributeEnum.UserEntityAttributeEnum;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.error.type.ChatErrorType;
import com.danielohagan.webapp.error.type.IErrorType;
import com.danielohagan.webapp.utils.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
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

    //TODO:: Maybe store an instance of the
    // ChatSession to contain the Message and User Lists

    private final int DEFAULT_MESSAGE_COUNT = 20; //Number of messages to load by default
    private final int CHAT_SESSION_ID;
    private final ChatSession mChatSession;

    private List<Session> mSessionList; //List of the WebSocket Clients
    private ChatSessionDAOImpl mChatSessionDAO;
    private MessageDAOImpl mMessageDAO;

    public ChatSessionHandler(int chatSessionId) {
        CHAT_SESSION_ID = chatSessionId;
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
        mSessionList = new ArrayList<>();

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

    public void addSession(Session session) {
        if (!mSessionList.contains(session)) {
            mSessionList.add(session);

            //Tell session to display existing messages
            for (Message message : mChatSession.getMessageList()) {
                JsonObject newMessage = generateDisplayMessageJson(message);
                sendToSession(session, newMessage);
            }

            //Tell session to display existing members
            for (User user : mChatSession.getUserList()) {
                JsonObject displayUserMessage = generateDisplayUserJson(user);
                sendToSession(session, displayUserMessage);
            }
        }
    }

    public JsonObject generateDisplayMessageJson(Message message) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ChatActionEnum.ACTION.getActionString(),
                        ChatActionEnum.DISPLAY_MESSAGE.getActionString()
                )
                .add(
                        MessageEntityAttributeEnum.MESSAGE.getAttributeString(),
                        serialiseMessage(message)
                )
                .build();
    }

    public JsonObject generateDisplayUserJson(User user) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
//                FIXME::
//                .add(
//                        "",
//                        user.getUsername()
//                )
                .add(
                        UserEntityAttributeEnum.STATUS.getAttributeString(),
                        user.getUserStatus().getDatabaseEnumStringValue()
                )
                .build();
    }

    /**
     * Remove a WebSocket Client session
     *
     * @param session WebSocket Client to remove
     */
    public void removeSession(Session session) {
        mSessionList.remove(session);
    }

    public void deleteMessage(int id) {
        Message message = getMessage(id);

        if (message != null) {
            mChatSession.getMessageList().remove(message);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add(
                            ChatActionEnum.ACTION.getActionString(),
                            ChatActionEnum.DELETE_MESSAGE.getActionString()
                    )
                    .add(
                            MessageEntityAttributeEnum.ID.getAttributeString(),
                            id
                    )
                    .build();

            sendToAllSessions(removeMessage);
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
            session.getBasicRemote().sendText(jsonObject.toString());
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
            int userId;

            try (JsonReader reader = Json.createReader(new StringReader(jsonMessage))) {
                JsonObject jsonObject = reader.readObject();

                body = jsonObject.getString(
                        MessageEntityAttributeEnum.BODY.getAttributeString()
                );
                userId = jsonObject.getInt(
                        MessageEntityAttributeEnum.SENDER_ID.getAttributeString()
                );
            }

            if (body != null && userId != 0) {
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
                sendToAllSessions(serialiseMessage(message));
            }
        }
    }

    public void deleteMessage(String message) {
        //TODO:: Remove from database

        //TODO:: Remove from mMessageList
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
            cleanUp();
        } else {
            sendInfo(
                    session,
                    ChatErrorType.PERMISSION_CHECK_FAILED_SESSION_DELETION
            );
        }
    }

    public void editMessage(String message) {

    }

    public JsonObject getMessageListAsJson() {
        JsonObject messageListJson = null;

        if (
                mChatSession.getMessageList() != null &&
                mChatSession.getMessageList().size() > 0
        ) {
            JsonProvider provider = JsonProvider.provider();
            JsonArray messageArrayJson = JsonArray.EMPTY_JSON_ARRAY;

            for (Message message : mChatSession.getMessageList()) {
                //TODO:: FIX THIS
//                messageArrayJson.add(serialiseMessage(message));
//                messageArrayJson.
            }

            messageListJson = provider.createObjectBuilder()
                    .add(
                            ChatActionEnum.ACTION.getActionString(),
                            ChatActionEnum.DISPLAY_MESSAGE_LIST.getActionString()
                    )
                    .add(
                            MessageEntityAttributeEnum.MESSAGE_LIST.getAttributeString(),
                            messageArrayJson
                    )
                    .build();
        }

        return messageListJson;
    }

    private JsonObject serialiseMessage(Message message) {
        JsonObject messageJson = null;

        if (message != null) {
            JsonProvider provider = JsonProvider.provider();

            messageJson = provider.createObjectBuilder()
                    .add(
                            MessageEntityAttributeEnum.ID.getAttributeString(),
                            message.getId()
                    )
                    .add(
                            MessageEntityAttributeEnum.BODY.getAttributeString(),
                            message.getBody()
                    )
                    .add(
                            MessageEntityAttributeEnum.SENDER_ID.getAttributeString(),
                            message.getSenderId()
                    )
//                    FIXME::
//                    .add(
//                            MessageEntityAttributeEnum.SENDER_USERNAME.getAttributeString(),
//                            new UserDAOImpl().getColumnStringsById(
//                                    message.getSenderId(),
//                                    UserDAOImpl.USERNAME_COLUMN_NAME
//                            ).get(UserDAOImpl.USERNAME_COLUMN_NAME)
//                    )
                    .add(
                            MessageEntityAttributeEnum.CHAT_SESSION_ID.getAttributeString(),
                            message.getChatSessionId()
                    )
                    .add(
                            MessageEntityAttributeEnum.CREATION_TIME.getAttributeString(),
                            message.getCreationTime().toString()
                    )
                    .build();
        }

        return messageJson;
    }

    private boolean isValidJsonMessage(String jsonMessage) {
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