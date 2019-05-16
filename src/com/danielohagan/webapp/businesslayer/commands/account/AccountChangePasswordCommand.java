package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.AccountErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountChangePasswordCommand extends AbstractCommand {

    private final String HTML_FORM_NEW_PASSWORD = "newPassword";
    private final String HTML_FORM_NEW_PASSWORD_CONFIRM = "newPasswordConfirm";
    private final String HTML_FORM_OLD_PASSWORD = "oldPassword";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        if (SessionManager.isLoggedIn(httpSession)) {
            String newPassword = request.getParameter(HTML_FORM_NEW_PASSWORD);
            String newPasswordConfirm = request.getParameter(HTML_FORM_NEW_PASSWORD_CONFIRM);
            String oldPassword = request.getParameter(HTML_FORM_OLD_PASSWORD);



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