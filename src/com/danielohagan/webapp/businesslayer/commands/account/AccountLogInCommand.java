package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.error.type.ErrorType;
import com.danielohagan.webapp.error.type.IErrorType;

import javax.servlet.ServletException;
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
            HttpServletResponse response
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        String inputEmail = request.getParameter(HTML_FORM_EMAIL);
        String inputPassword = request.getParameter(HTML_FORM_PASSWORD);

        //Check the account's input to see if the inputs are valid and if there is a corresponding account
        IErrorType errorType = userDAO.userLoginGetErrorType(
                inputEmail,
                inputPassword
        );

        User user = userDAO.getUserByEmail(inputEmail);

        try {
            if (errorType == ErrorType.NO_ERROR) {

                SessionManager.logInUser(httpSession, user);

                request.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                        .forward(request, response);
            } else {
                setRequestError(request, errorType);

                SessionManager.logInUser(httpSession, user);

                request.getRequestDispatcher(JSPFileMap.ACCOUNT_LOG_IN_PAGE)
                        .forward(request, response);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}