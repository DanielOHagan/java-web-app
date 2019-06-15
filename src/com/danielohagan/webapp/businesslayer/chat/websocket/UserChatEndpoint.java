package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.utils.Random;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/chat")
public class UserChatEndpoint {

    private static final Map<Integer, ChatSessionHandler> mChatSessionMap = new HashMap<>();

    //private ChatSessionHandler mChatSessionHandler = new ChatSessionHandler();
    private ChatSessionDAOImpl mChatSessionDAO = new ChatSessionDAOImpl();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Socket Open");
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println("Socket Received Message: " + message);

        ServerActionEnum action = WebSocketMessageUtils.decodeMessageAction(message);
        Integer chatSessionId = WebSocketMessageUtils.getChatSessionId(message);

        switch (action) {
            //TODO:: Validation for EACH action
            case INIT:
                initChatSession(message, session);
                break;
            case CLOSE:
                closeSession(message, session);
                break;
            case ADD_MESSAGE:
                mChatSessionMap.get(chatSessionId).addNewMessage(message);
                break;
            case DELETE_MESSAGE:
                mChatSessionMap.get(chatSessionId).deleteMessage(message);
                break;
            case EDIT_MESSAGE:
                mChatSessionMap.get(chatSessionId).editMessage(message, session);
                break;
            case ADD_USER:
                mChatSessionMap.get(chatSessionId).addUser(message);
                break;
            case REMOVE_USER:
                mChatSessionMap.get(chatSessionId).removeUser(message);
                break;
            case CHANGE_USER_PERMISSION:
                mChatSessionMap.get(chatSessionId).updateUserPermission(message);
                break;
            case CREATE_NEW_CHAT_SESSION:
                Integer creatorId = WebSocketMessageUtils.getUserId(message);
                createNewChatSession(creatorId, session);
                break;
            case DELETE_CHAT_SESSION:
                mChatSessionMap.get(chatSessionId).deleteChatSession(message, session);
                mChatSessionMap.remove(chatSessionId);
                break;
            case INFO:
//                System.out.println("INFO: " + getInfoMessage(message));
                break;
            case ERROR:
//                System.out.println("ERROR: " + getErrorMessage(message));
                break;
            case CLOSE_PREVIOUS_CHAT_SESSION:
                mChatSessionMap.get(chatSessionId).removeSession(session);
                break;
            case NO_ACTION:
                System.err.println("Message received but was given no action, or no action could be read.");
                break;
            case ACTION:
                //Since no action was specified, maybe log the message?
                break;

            default:
                System.err.println("Unrecognised action string: " + action.getAttributeString());
                break;
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
        System.out.println("Socket Close");

        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initChatSession(String message, Session session) {
        Integer chatSessionId = WebSocketMessageUtils.getChatSessionId(message);
        Integer userId = WebSocketMessageUtils.getUserId(message);

        if (chatSessionId != null && userId != null) {
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
        Integer chatSessionId = WebSocketMessageUtils.getChatSessionId(message);

        if (chatSessionId != null) {
            if (mChatSessionMap.get(chatSessionId) != null) {
                mChatSessionMap.get(chatSessionId).removeSession(session);
            }
        }
    }

    private void createNewChatSession(Integer creatorId, Session session) {
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
            ChatSessionHandler chatSessionHandler = new ChatSessionHandler(chatSession);
            chatSessionHandler.addSession(session);
            chatSessionHandler.createChatSession(creatorId, session);

            mChatSessionMap.put(chatSessionId, new ChatSessionHandler(chatSession));
        }
    }
}