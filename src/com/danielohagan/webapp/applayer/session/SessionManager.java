package com.danielohagan.webapp.applayer.session;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;

import javax.servlet.http.HttpSession;
import java.util.List;

public class SessionManager {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String LOGGED_IN = "loggedIn";
    private static final String ATTRIBUTE_USER = "currentUser";
    private static final String ATTRIBUTE_CHAT_SESSION_LIST = "chatSessionList";
    private static final String ATTRIBUTE_CHAT_SESSION_MESSAGE_LIST = "chatSessionMessageList";
    private static final String ATTRIBUTE_PRIMARY_CHAT_SESSION = "primaryChatSession";

    //Time before user is logged out for inactivity, in seconds
    private static final int MAX_INACTIVE_INTERVAL = 3_600;


    public static void setCurrentUser(
            HttpSession httpSession,
            User user
    ) {
        httpSession.setAttribute(ATTRIBUTE_USER, user);
        httpSession.setAttribute(LOGGED_IN, TRUE);
        httpSession.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    }

    public static boolean isLoggedIn(HttpSession httpSession) {
        if (httpSession != null) {
            return httpSession.getAttribute(LOGGED_IN) == TRUE;
        }

        return false;
    }

    public static void removeCurrentUser(HttpSession httpSession) {
        httpSession.setAttribute(LOGGED_IN, FALSE);
        httpSession.removeAttribute(ATTRIBUTE_USER);
    }

    public static User getCurrentUser(HttpSession httpSession) {
        if (httpSession != null) {
            return (User) httpSession.getAttribute(ATTRIBUTE_USER);
        }

        return null;
    }

    public static void setDefault(HttpSession httpSession) {
        httpSession.setAttribute(LOGGED_IN, FALSE);
        httpSession.removeAttribute(ATTRIBUTE_USER);
    }

    public static void setChatSessionList(
            HttpSession httpSession,
            List<ChatSession> chatSessionList
    ) {
        httpSession.setAttribute(
                ATTRIBUTE_CHAT_SESSION_LIST,
                chatSessionList
        );
    }
}