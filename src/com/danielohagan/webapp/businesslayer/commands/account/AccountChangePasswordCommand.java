package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.ErrorType;
import com.danielohagan.webapp.error.type.IErrorType;
import com.danielohagan.webapp.error.type.SessionErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountChangePasswordCommand extends AbstractCommand {

    private final String HTML_FORM_NEW_PASSWORD = "newPassword";
    private final String HTML_FORM_NEW_PASSWORD_CONFIRM = "newPasswordConfirm";
    private final String HTML_FORM_OLD_PASSWORD = "oldPassword";

    private final String CHANGE_PASSWORD_SUCCESS_MESSAGE = "Password successfully changed";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();
        IErrorType errorType;

        if (SessionManager.isLoggedIn(httpSession)) {
            User user = SessionManager.getCurrentUser(httpSession);

            if (user != null) {
                int id = user.getId();
                String newPassword = request.getParameter(HTML_FORM_NEW_PASSWORD);
                String newPasswordConfirm = request.getParameter(HTML_FORM_NEW_PASSWORD_CONFIRM);
                String oldPassword = request.getParameter(HTML_FORM_OLD_PASSWORD);

                errorType = userDAO.changePasswordGetErrorType(
                        id,
                        newPassword,
                        newPasswordConfirm,
                        oldPassword
                );

                if (errorType == ErrorType.NO_ERROR) {

                    userDAO.updatePassword(id, newPassword);

                    setRequestInfo(request, CHANGE_PASSWORD_SUCCESS_MESSAGE);

                    try {
                        request.getRequestDispatcher(JSPFileMap.ACCOUNT_SETTINGS_JSP)
                                .forward(request, response);
                    } catch (ServletException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    forwardWithError(
                            request,
                            response,
                            errorType,
                            JSPFileMap.ACCOUNT_SETTINGS_JSP
                    );
                }

            } else {
                forwardWithError(
                        request,
                        response,
                        SessionErrorType.FAILED_TO_RETRIEVE_CURRENT_USER,
                        JSPFileMap.ACCOUNT_SETTINGS_JSP
                );
            }
        } else {
            forwardWithError(
                    request,
                    response,
                    AccountErrorType.NOT_LOGGED_IN,
                    JSPFileMap.INDEX_JSP
            );
        }
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            IErrorType errorType,
            String requestDispatcher
    ) {
        setRequestError(request, errorType);

        try {
            request.getRequestDispatcher(requestDispatcher)
                    .forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}