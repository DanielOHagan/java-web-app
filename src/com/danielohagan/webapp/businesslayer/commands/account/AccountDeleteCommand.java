package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountDeleteCommand extends AbstractCommand {

    private final String HTML_FORM_PASSWORD = "deleteAccountPassword";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        ChatSessionDAOImpl chatSessionDAO = new ChatSessionDAOImpl();
        HttpSession httpSession = request.getSession();
        String password = request.getParameter(HTML_FORM_PASSWORD);

        //Delete the user
        if (SessionManager.isLoggedIn(httpSession)) {
            int id = SessionManager.getCurrentUser(httpSession).getId();

            if (userDAO.isCorrectPassword(id, password)) {
                userDAO.deleteUser(id);

                //TODO:: Delete from Chat Sessions
            } else {
                errorResponse.add(AccountErrorType.INPUT_PASSWORD_INCORRECT);
            }

            if (userDAO.getById(id) != null) {
                errorResponse.add(AccountErrorType.DELETION_FAILED);
            } else {
                //Remove session attributes
                SessionManager.setDefault(httpSession);
            }
        } else {
            errorResponse.add(AccountErrorType.NOT_LOGGED_IN);
        }

        loadPage(request, response, errorResponse, JSPFileMap.INDEX_JSP);
    }
}