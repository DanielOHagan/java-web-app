package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountLogInCommand extends AbstractCommand {

    private final String HTML_FORM_EMAIL = "loginFormEmail";
    private final String HTML_FORM_PASSWORD = "loginFormPassword";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        String inputEmail = request.getParameter(HTML_FORM_EMAIL);
        String inputPassword = request.getParameter(HTML_FORM_PASSWORD);

        errorResponse.add(userDAO.userLoginGetErrors(
                inputEmail,
                inputPassword
        ));

        User user = userDAO.getUserByEmail(inputEmail);

        try {
            if (!errorResponse.containsSeverity(ErrorSeverity.MINOR)) {

                SessionManager.setCurrentUser(httpSession, user);

                response.sendRedirect(request.getContextPath() + "/");
            } else {

                SessionManager.setDefault(httpSession);

                loadPage(
                        request,
                        response,
                        errorResponse,
                        JSPFileMap.ACCOUNT_LOG_IN_PAGE
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}