package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.error.AccountErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountUpdateCommand extends AbstractCommand {

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession httpSession = request.getSession();

        if (SessionManager.isLoggedIn(httpSession)) {

            //Update the

            try {
                request.getRequestDispatcher(JSPFileMap.ACCOUNT_SETTINGS_JSP)
                        .forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            request.setAttribute(
                    REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                    AccountErrorType.NOT_LOGGED_IN
            );

            try {
                request.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                        .forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}