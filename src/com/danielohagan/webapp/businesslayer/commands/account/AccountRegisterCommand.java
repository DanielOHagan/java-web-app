package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.utils.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class AccountRegisterCommand extends AbstractCommand {

    private static final String HTML_FORM_USERNAME = "registerFormUsername";
    private static final String HTML_FORM_EMAIL = "registerFormEmail";
    private static final String HTML_FORM_EMAIL_CONFIRM = "registerFormEmailConfirm";
    private static final String HTML_FORM_PASSWORD = "registerFormPassword";
    private static final String HTML_FORM_PASSWORD_CONFIRM = "registerFormPasswordConfirm";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        String username = request.getParameter(HTML_FORM_USERNAME);
        String email = request.getParameter(HTML_FORM_EMAIL);
        String emailConfirm = request.getParameter(HTML_FORM_EMAIL_CONFIRM);
        String password = request.getParameter(HTML_FORM_PASSWORD);
        String passwordConfirm = request.getParameter(HTML_FORM_PASSWORD_CONFIRM);

        errorResponse.add(userDAO.userRegisterGetErrors(
                username,
                email,
                emailConfirm,
                password,
                passwordConfirm
        ));

        if (!errorResponse.containsSeverity(ErrorSeverity.MINOR)) {
            //Create and store the user
            User user = new User();

            int id = Random.generateRandomPositiveInt();

            while (userDAO.exists(id)) {
                id = Random.generateRandomPositiveInt();
            }

            user.setId(id);

            user.setEmail(email);
            user.setUsername(username);
            user.setUserStatus(UserStatus.NEW);
            user.setCreationTime(LocalDateTime.now());
            userDAO.createNewUser(user, password);

            //Check that the user was created on the database
            user = userDAO.getByEmailAndPassword(email, password);

            if (user == null) {
                errorResponse.add(AccountErrorType.CREATION_FAILED);
                loadPage(
                        request,
                        response,
                        errorResponse,
                        request.getContextPath() + "/account/register"
                );
            } else {
                //Log in to the HTTP Session
                SessionManager.setCurrentUser(httpSession, user);
                errorResponse.add(AccountErrorType.SUCCESSFUL_CREATION);
                loadPage(request, response, errorResponse, JSPFileMap.INDEX_JSP);
            }

        } else {
            loadPage(
                    request,
                    response,
                    errorResponse,
                    JSPFileMap.ACCOUNT_REGISTER_JSP
            );
        }
    }
}