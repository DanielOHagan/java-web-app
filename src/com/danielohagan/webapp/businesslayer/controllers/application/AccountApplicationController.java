package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.businesslayer.commands.account.*;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.ApplicationControllerErrorType;
import com.danielohagan.webapp.error.type.ErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountApplicationController extends AbstractApplicationController {

    private final String DELETE_KEY = "delete";
    private final String LOG_IN_KEY = "login";
    private final String LOG_OUT_KEY = "logout";
    private final String REGISTER_KEY = "register";
    private final String PROFILE_KEY = "profile";
    private final String SETTINGS_KEY = "settings";
    private final String CHANGE_PASSWORD_KEY = "change-password";

    private final String URL_PARAM_ID = "id";

    private final String REQUEST_ATTRIB_PROFILE_USER = "profileUser";

    private final String ACCOUNT_URL_PATTERN = "account/";

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private UserDAOImpl mUserDAO;
    private String mKey;
    private Map<String, Class> mCommandMap;

    public AccountApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
        mCommandMap = new HashMap<>();
        mUserDAO = new UserDAOImpl();

        mCommandMap.put(DELETE_KEY, AccountDeleteCommand.class);
        mCommandMap.put(LOG_IN_KEY, AccountLogInCommand.class);
        mCommandMap.put(LOG_OUT_KEY, AccountLogOutCommand.class);
        mCommandMap.put(REGISTER_KEY, AccountRegisterCommand.class);
        mCommandMap.put(CHANGE_PASSWORD_KEY, AccountChangePasswordCommand.class);
    }

    @Override
    public void processPost() {

        processURL();

        //Execute Command
        AbstractCommand command;

        switch (mKey) {
            case LOG_IN_KEY:
                command = new AccountLogInCommand();
                break;
            case LOG_OUT_KEY:
                command = new AccountLogOutCommand();
                break;
            case CHANGE_PASSWORD_KEY:
                command = new AccountChangePasswordCommand();
                break;
            case REGISTER_KEY:
                command = new AccountRegisterCommand();
                break;
            case DELETE_KEY:
                command = new AccountDeleteCommand();
                break;
            default:
                command = new ErrorCommand();
                command.setRequestError(
                        mRequest,
                        ApplicationControllerErrorType.COMMAND_CLASS_NOT_FOUND
                );
                break;
        }

        command.execute(mRequest, mResponse);
    }

    @Override
    public void processGet() {

        processURL();

        try {
            if (mKey == null) {
                mRequest.setAttribute(
                        REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                        ErrorType.HTTP_RESPONSE_CODE_404
                );

                mRequest.getRequestDispatcher(JSPFileMap.ERROR_JSP)
                        .forward(mRequest, mResponse);
            } else {
                switch (mKey) {
                    case PROFILE_KEY:

                        setProfilePageAttribs();

                        mRequest.getRequestDispatcher(JSPFileMap.ACCOUNT_PROFILE_JSP)
                                .forward(mRequest, mResponse);
                        break;
                    case LOG_IN_KEY:

                        //Forward the user to Home if they are already logged in
                        // (They shouldn't be given a link to the login page if they are already logged in)

                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            mRequest.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                                    .forward(mRequest, mResponse);
                        } else {
                            mRequest.getRequestDispatcher(JSPFileMap.ACCOUNT_LOG_IN_PAGE)
                                    .forward(mRequest, mResponse);
                        }
                        break;
                    case LOG_OUT_KEY:
                        //Log out the user if key is LOG_OUT and user is logged in
                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            AccountLogOutCommand command = new AccountLogOutCommand();
                            command.execute(mRequest, mResponse);
                        } else {
                            mResponse.sendRedirect(mRequest.getContextPath());
                        }
                        break;
                    case SETTINGS_KEY:

                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            mRequest.getRequestDispatcher(JSPFileMap.ACCOUNT_SETTINGS_JSP)
                                    .forward(mRequest, mResponse);
                        } else {
                            mRequest.setAttribute(
                                    REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                                    AccountErrorType.NOT_LOGGED_IN
                            );

                            mRequest.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                                    .forward(mRequest, mResponse);

                        }
                        break;
                    case REGISTER_KEY:
                        mRequest.getRequestDispatcher(JSPFileMap.ACCOUNT_REGISTER_JSP)
                                .forward(mRequest, mResponse);
                        break;
                    default:
                        mRequest.setAttribute(
                                REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                                ErrorType.HTTP_RESPONSE_CODE_404
                        );
                        mRequest.getRequestDispatcher(JSPFileMap.ERROR_JSP)
                                .forward(mRequest, mResponse);
                        break;
                }
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processURL() {
        //Get request specific data from the URL

        String uri = mRequest.getRequestURI();
        mKey = null;

        if (uri.startsWith("/")) {
            uri = uri.replaceFirst("/", "");
        }

        //Look for key 'account/(mKey)'
        if (uri.contains(ACCOUNT_URL_PATTERN)) {
            String accountUri = uri.substring(uri.lastIndexOf(ACCOUNT_URL_PATTERN));

            if (accountUri.split("/").length > 1) {
                mKey = accountUri.split("/")[1].toLowerCase();
            }
        }
    }

    private void setProfilePageAttribs() {
        int userId = Integer.parseInt(mRequest.getParameter(URL_PARAM_ID));

        mRequest.setAttribute(
                REQUEST_ATTRIB_PROFILE_USER,
                mUserDAO.getById(userId)
        );
    }
}