package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountLogOutCommand extends AbstractCommand {

    private static final String SUCCESSFUL_LOG_OUT = "Successfully logged out of your account";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        HttpSession httpSession = request.getSession();

        //Remove session attributes
        if (SessionManager.isLoggedIn(request.getSession())) {
            SessionManager.removeCurrentUser(httpSession);
            errorResponse.add(AccountErrorType.SUCCESSFUL_LOG_OUT);

            loadPage(request, response, errorResponse, JSPFileMap.INDEX_JSP);
        } else {
            redirectToHome(request, response);
        }
    }
}