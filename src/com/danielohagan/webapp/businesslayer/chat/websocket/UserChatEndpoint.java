package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.json.ChatMessageJsonDecoder;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.utils.Random;

import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/chat")
public class UserChatEndpoint {

    private static final Map<Integer, ChatSessionHandler> mChatSessionMap = new HashMap<>();

    private ChatSessionDAOImpl mChatSessionDAO = new ChatSessionDAOImpl();

    @OnOpen
    public void onOpen(Session session) {
        if (session != null) {
            System.out.println("Socket Open: " + session.getId());

        } else {
            System.out.println("Socket Open: UNKNOWN SESSION");
        }
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        if (session != null && message != null) {
            ChatMessageJsonDecoder chatMessageJsonDecoder = new ChatMessageJsonDecoder();
            ServerActionEnum action = chatMessageJsonDecoder.decodeMessageAction(message);
            Integer chatSessionId = chatMessageJsonDecoder.getChatSessionId(message);
            ChatSessionHandler chatSessionHandler = null;

            System.out.println("Socket: " + session.getId() + " -- Received Message: " + message);

            if (chatSessionId != null) {
                chatSessionHandler = mChatSessionMap.get(chatSessionId);
            }

            switch (action) {
                //TODO:: Validation for EACH action
                case INIT:
                    Integer userId = chatMessageJsonDecoder.getUserId(message);
                    initChatSession(chatSessionId, userId, session);
                    break;
                case CLOSE:
                    closeSession(message, session);
                    break;
                case HEART_BEAT:
                    //Heart beat action to keep connection alive
                    break;

                case ADD_MESSAGE:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.addNewMessage(message);
                    }
                    break;
                case DELETE_MESSAGE:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.deleteMessage(message);
                    }
                    break;
                case EDIT_MESSAGE:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.editMessage(message, session);
                    }
                    break;
                case ADD_USER:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.addUser(message);
                    }
                    break;
                case REMOVE_USER:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.removeUser(message);
                    }
                    break;
                case CHANGE_USER_PERMISSION:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.updateUserPermission(message);
                    }
                    break;
                case CREATE_NEW_CHAT_SESSION:
                    Integer creatorId = chatMessageJsonDecoder.getUserId(message);
                    ChatSessionHandler csh = createNewChatSessionHandler(creatorId, session);

                    if (csh != null) {
                        initChatSession(csh.getChatSessionId(), creatorId, session);
                        mChatSessionMap.get(csh.getChatSessionId()).sendSetChatSessionId(session);
                    } else {
                        //Send Error saying failed to create new Chat Session
                    }
                    break;
                case DELETE_CHAT_SESSION:
                    if (chatSessionHandler != null) {
                        //If chat session is loaded
                        chatSessionHandler
                                .deleteChatSession(message, session);
                    } else if (
                            chatSessionId != null &&
                            mChatSessionDAO.exists(chatSessionId)
                    ){
                        //If chat session is not loaded
                        mChatSessionDAO.deleteSession(chatSessionId);
                    }

                    mChatSessionMap.remove(chatSessionId);
                    break;

                case INFO:
//            System.out.println("INFO: " + chatMessageJsonDecoder.getInfoMessage(message));
                    break;
                case ERROR:
//            System.err.println("ERROR: " + chatMessageJsonDecoder.getErrorMessage(message));
                    break;

                case CLOSE_PREVIOUS_CHAT_SESSION:
                    if (chatSessionHandler != null) {
                        chatSessionHandler.removeSession(session);
                    }
                    break;
                case NO_ACTION:
                    System.err.println("Message received but was given no action, or no action could be read.");
                    break;
                case ACTION:
                    //TODO:: Since no action was specified, maybe log the message?
                    break;

                default:
                    System.err.println("Unrecognised action string: " + action.toString());
                    break;
            }
        } else {
            System.err.println("Session or message is NULL");
        }

        return message;
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("Socket Error: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        if (session != null) {

            System.out.println("Socket Close: " + session.getId());

            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Socket Close: UNKNOWN SESSION");
        }
    }

    private void initChatSession(Integer chatSessionId, Integer userId, Session session) {
        if (chatSessionId != null && userId != null) {
            //Link Chat Session with websocket client session
            if (
                    mChatSessionDAO.exists(chatSessionId) &&
                    mChatSessionDAO.userIsInChatSession(userId, chatSessionId)
            ) {
                if (!mChatSessionMap.containsKey(chatSessionId)) {
                    ChatSessionHandler chatSessionHandler =
                            new ChatSessionHandler(chatSessionId);
                    mChatSessionMap.put(chatSessionId, chatSessionHandler);
                }
                mChatSessionMap.get(chatSessionId).addSession(session);
            }
        }
    }

    private void closeSession(String message, Session session) {
        ChatMessageJsonDecoder chatMessageJsonDecoder = new ChatMessageJsonDecoder();
        Integer chatSessionId = chatMessageJsonDecoder.getChatSessionId(message);

        //Remove session from Chat Session
        if (chatSessionId != null) {
            if (mChatSessionMap.get(chatSessionId) != null) {
                mChatSessionMap.get(chatSessionId).removeSession(session);
            }
        }
    }

    private ChatSessionHandler createNewChatSessionHandler(Integer creatorId, Session session) {
        ChatSessionHandler chatSessionHandler = null;

        if (creatorId != null) {
            int chatSessionId = Random.generateRandomPositiveInt();

            while (mChatSessionDAO.exists(chatSessionId)) {
                chatSessionId = Random.generateRandomPositiveInt();
            }

            ChatSession chatSession = new ChatSession(
                    chatSessionId,
                    "Chat Session: " + chatSessionId,
                    LocalDateTime.now()
            );
            chatSessionHandler = new ChatSessionHandler(chatSession);
            chatSessionHandler.addSession(session);
            chatSessionHandler.createChatSession(creatorId, session);
        }

        return chatSessionHandler;
    }

    public void sendToAllSessions(JsonObject jsonObject, List<Session> sessionList) {
        for (Session session : sessionList) {
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
}