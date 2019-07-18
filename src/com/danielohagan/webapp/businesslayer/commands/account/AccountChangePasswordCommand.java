package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.SessionErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountChangePasswordCommand extends AbstractCommand {

    private final String HTML_FORM_NEW_PASSWORD = "newPassword";
    private final String HTML_FORM_NEW_PASSWORD_CONFIRM = "newPasswordConfirm";
    private final String HTML_FORM_OLD_PASSWORD = "oldPassword";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        if (SessionManager.isLoggedIn(httpSession)) {
            User user = SessionManager.getCurrentUser(httpSession);

            if (user != null) {
                int id = user.getId();
                String newPassword = request.getParameter(HTML_FORM_NEW_PASSWORD);
                String newPasswordConfirm = request.getParameter(HTML_FORM_NEW_PASSWORD_CONFIRM);
                String oldPassword = request.getParameter(HTML_FORM_OLD_PASSWORD);

                errorResponse.add(userDAO.changePasswordGetErrors(
                        id,
                        newPassword,
                        newPasswordConfirm,
                        oldPassword
                ));

                if (!errorResponse.containsSeverity(ErrorSeverity.MINOR)) {

                    userDAO.updatePassword(id, newPassword);
                    errorResponse.add(AccountErrorType.CHANGE_PASSWORD_SUCCESS);
                    loadPage(
                            request,
                            response,
                            errorResponse,
                            JSPFileMap.ACCOUNT_SETTINGS_JSP
                    );
                } else {
                    loadPage(
                            request,
                            response,
                            errorResponse,
                            JSPFileMap.ACCOUNT_SETTINGS_JSP
                    );
                }

            } else {
                errorResponse.add(SessionErrorType.FAILED_TO_RETRIEVE_CURRENT_USER);
                loadPage(
                        request,
                        response,
                        errorResponse,
                        JSPFileMap.ACCOUNT_SETTINGS_JSP
                );
            }
        } else {
            errorResponse.add(AccountErrorType.NOT_LOGGED_IN);
            loadPage(
                    request,
                    response,
                    errorResponse,
                    JSPFileMap.INDEX_JSP
            );
        }
    }
}