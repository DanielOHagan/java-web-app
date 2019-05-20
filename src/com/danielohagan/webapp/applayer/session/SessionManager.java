package com.danielohagan.webapp.applayer.session;

import com.danielohagan.webapp.businesslayer.entities.account.User;

import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String LOGGED_IN = "loggedIn";
    private static final String ATTRIBUTE_USER = "currentUser";

    //Time before user is logged out for inactivity, in seconds
    private static final int MAX_INACTIVE_INTERVAL = 3_600;

    public static void logInUser(
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

    public static void logOutUser(HttpSession httpSession) {
        httpSession.setAttribute(LOGGED_IN, FALSE);
        httpSession.removeAttribute(ATTRIBUTE_USER);
    }

    public static User getCurrentUser(HttpSession httpSession) {
        if (httpSession != null) {
            return (User) httpSession.getAttribute(ATTRIBUTE_USER);
        }

        return null;
    }
}