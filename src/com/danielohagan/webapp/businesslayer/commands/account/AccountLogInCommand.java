package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.error.ErrorType;
import com.danielohagan.webapp.error.IErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountLogInCommand extends AbstractCommand {

    /*
     * HTML_FORM_USERNAME
     *  The name of the HTML form tag
     *
     * HTML_FORM_PASSWORD
     *  The name of the HTML form tag
     */
    private static final String HTML_FORM_EMAIL = "loginFormEmail";
    private static final String HTML_FORM_PASSWORD = "loginFormPassword";

    /*
    TODO:: Check if the inputs are correct
     If so then log the user in
     Start the user session
     Then redirect the user
     */

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

//                setRequestAttributes(request, user);
                SessionManager.setSessionUserAttributes(httpSession, user);

                request.getRequestDispatcher(JSPFileMap.HOME_PAGE)
                        .forward(request, response);
            } else {
                //Set the error message
                request.setAttribute(
                        REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                        errorType.getErrorMessage()
                );

                SessionManager.setSessionUserAttributes(httpSession, user);

                request.getRequestDispatcher(JSPFileMap.ACCOUNT_LOG_IN_PAGE)
                        .forward(request, response);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRequestAttributes(
            HttpServletRequest request,
            User user
    ) {
        request.setAttribute(
                REQUEST_ATTRIBUTE_LOGGED_IN,
                user != null ? REQUEST_TRUE : REQUEST_FALSE
        );
    }
}