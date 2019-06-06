package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(
        value = "/chat"

)
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

        ChatActionEnum action = WebSocketMessageUtils.decodeMessageAction(message);
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
//                mChatSessionMap.get(chatSessionId)
//                        .deleteMessage(WebSocketMessageUtils.getMessageId(message));
                break;
            case EDIT_MESSAGE:
//                mChatSessionMap.get(chatSessionId).editMessage(message);
                break;
            case ADD_USER:
//                mChatSessionMap.get(chatSessionId).addUser(message)
                break;
            case REMOVE_USER:

                break;
//            case CREATE_NEW_CHAT_SESSION:
//                TODO::
//                  Chat Sessions are displayed through a HTTP Request,
//                  I think that needs to change to WebSocket,
//                  Or if I'm too lazy, then sent an action message to
//                  JS client to refresh the page
//                break;
            case DELETE_CHAT_SESSION:
//                TODO::
//                  Chat Sessions are displayed through a HTTP Request,
//                  I think that needs to change to WebSocket,
//                  Or if I'm too lazy, then sent an action message to
//                  JS client to refresh the page
                mChatSessionMap.get(chatSessionId).deleteChatSession(message, session);
                mChatSessionMap.remove(chatSessionId);
                break;
            case INFO:
//                System.out.println("INFO: " + getInfoMessage(message));
                break;
            case ERROR:
//                System.out.println("ERROR: " + getErrorMessage(message));
                break;
            case DISPLAY_MESSAGE_LIST:
                //Display a list of messages
                break;
            case DISPLAY_MESSAGE:
                //Display a single message
                break;
            case DISPLAY_USER:
                //Display a user
                break;
            case NO_ACTION:
                System.out.println("Message received but was given no action, or no action could be read.");
                break;
            case ACTION:
                //Since no action was specified, maybe log the message?
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

    public void initChatSession(String message, Session session) {
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

    public void closeSession(String message, Session session) {
        Integer chatSessionId = WebSocketMessageUtils.getChatSessionId(message);

        if (chatSessionId != null) {
            if (mChatSessionMap.get(chatSessionId) != null) {
                mChatSessionMap.get(chatSessionId).removeSession(session);
            }
        }
    }
}