package com.danielohagan.webapp.businesslayer.commands.account;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccountLogOutCommand extends AbstractCommand {

    private static final String SUCCESSFUL_LOG_OUT = "Successfully logged out of your account";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpSession httpSession = request.getSession();

        //Remove session attributes
        if (SessionManager.isLoggedIn(request.getSession())) {
            SessionManager.logOutUser(httpSession);
            setRequestInfo(request, SUCCESSFUL_LOG_OUT);

            try {
                request.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                        .forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}