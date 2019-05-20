package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.type.AccountErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountDeleteCommand extends AbstractCommand {

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserDAOImpl userDAO = new UserDAOImpl();
        HttpSession httpSession = request.getSession();

        //Delete the user
        if (SessionManager.isLoggedIn(httpSession)) {
            int id = SessionManager.getCurrentUser(httpSession).getId();

            userDAO.deleteUser(id);

            if (userDAO.getById(id) != null) {
                setRequestError(request, AccountErrorType.DELETION_FAILED);
            } else {
                //Remove session attributes
                SessionManager.logOutUser(httpSession);
            }

        } else {
            setRequestError(request, AccountErrorType.NOT_LOGGED_IN);
        }

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