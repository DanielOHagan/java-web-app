package com.danielohagan.webapp.applayer.session;

import com.danielohagan.webapp.businesslayer.entities.account.User;

import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final String LOGGED_IN = "loggedIn";

    private static final String ATTRIBUTE_USER = "currentUser";

    public static void setSessionUserAttributes(
            HttpSession httpSession,
            User user
    ) {
        if (user != null) {
            httpSession.setAttribute(ATTRIBUTE_USER, user);
            httpSession.setAttribute(LOGGED_IN, TRUE);
        } else {
            httpSession.setAttribute(LOGGED_IN, FALSE);
        }
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
}