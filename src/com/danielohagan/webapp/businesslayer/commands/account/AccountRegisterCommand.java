package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.ErrorType;
import com.danielohagan.webapp.error.type.IErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

public class AccountRegisterCommand extends AbstractCommand {

    private static final String HTML_FORM_USERNAME = "registerFormUsername";
    private static final String HTML_FORM_EMAIL = "registerFormEmail";
    private static final String HTML_FORM_EMAIL_CONFIRM = "registerFormEmailConfirm";
    private static final String HTML_FORM_PASSWORD = "registerFormPassword";
    private static final String HTML_FORM_PASSWORD_CONFIRM = "registerFormPasswordConfirm";

    private static final String SUCCESSFUL_REGISTER = "Successfully created an account";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        String username = request.getParameter(HTML_FORM_USERNAME);
        String email = request.getParameter(HTML_FORM_EMAIL);
        String emailConfirm = request.getParameter(HTML_FORM_EMAIL_CONFIRM);
        String password = request.getParameter(HTML_FORM_PASSWORD);
        String passwordConfirm = request.getParameter(HTML_FORM_PASSWORD_CONFIRM);

        IErrorType errorType = userDAO.userRegisterGetErrorType(
                username,
                email,
                emailConfirm,
                password,
                passwordConfirm
        );

        if (errorType == ErrorType.NO_ERROR) {
            //Create and store the user
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setUserStatus(UserStatus.NEW);
            user.setCreationTime(LocalDateTime.now());
            userDAO.createNewUser(user, password);

            //Check that the user was created on the database
            user = userDAO.getByEmailAndPassword(email, password);

            if (user == null) {

                setRequestError(request, AccountErrorType.CREATION_FAILED);

                try {
                    request.getRequestDispatcher(JSPFileMap.ACCOUNT_REGISTER_JSP)
                            .forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Log in to the HTTP Session
                SessionManager.logInUser(httpSession, user);

                setRequestInfo(request, SUCCESSFUL_REGISTER);

                try {
                    request.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                            .forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {

            setRequestError(request, errorType);

            try {
                request.getRequestDispatcher(JSPFileMap.ACCOUNT_REGISTER_JSP)
                        .forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}