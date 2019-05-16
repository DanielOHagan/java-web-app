package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.businesslayer.commands.account.*;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.AccountErrorType;
import com.danielohagan.webapp.error.ApplicationControllerErrorType;
import com.danielohagan.webapp.error.ErrorType;
import com.danielohagan.webapp.error.IErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

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

    public AccountApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;

        //Initialise or clear Command Map the populate with Controller related Commands
        if (mCommandMap == null) {
            mCommandMap = new HashMap<>();
        } else {
            mCommandMap.clear();
        }

        mCommandMap.put(DELETE_KEY, AccountDeleteCommand.class);
        mCommandMap.put(LOG_IN_KEY, AccountLogInCommand.class);
        mCommandMap.put(LOG_OUT_KEY, AccountLogOutCommand.class);
        mCommandMap.put(REGISTER_KEY, AccountRegisterCommand.class);

        mUserDAO = new UserDAOImpl();
    }

    @Override
    public void processPost() {

        IErrorType errorType;

        //TODO:: I think this URL validation code should eventually be done by a filter
        if ((errorType = getURLErrorType()) != ErrorType.NO_ERROR) {
            new ErrorCommand().execute(mRequest, mResponse, errorType);
        }

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
                break;
        }

        command.execute(mRequest, mResponse);
    }

    @Override
    public void processGet() {
        IErrorType errorType;

        processURL();

        //TODO:: I think this URL validation code should eventually be done by a filter
        if ((errorType = getURLErrorType()) != ErrorType.NO_ERROR) {
            new ErrorCommand().execute(mRequest, mResponse, errorType);
        }

        try {
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
                    }

                    mRequest.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                            .forward(mRequest, mResponse);
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
                            ErrorType.HTTP_RESPONSE_CODE_404.getErrorMessage()
                    );
                    mRequest.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                            .forward(mRequest, mResponse);
                    break;
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

        if (uri.startsWith("/")) {
            uri = uri.replaceFirst("/", "");
        }

        String accountUri = uri.substring(uri.lastIndexOf(ACCOUNT_URL_PATTERN));

        mKey = accountUri.split("/")[1].toLowerCase();
    }

    private IErrorType getURLErrorType() {

        //Check the retrieved key has a corresponding command
        if (!mCommandMap.keySet().contains(mKey)) {
            return ApplicationControllerErrorType.COMMAND_CLASS_NOT_FOUND;
        }

        return ErrorType.NO_ERROR;
    }

    private void setAttributes() {
        //TODO:: Session attributes

        //TODO:: Request attributes
    }

    private void setProfilePageAttribs() {
        int userId = Integer.parseInt(mRequest.getParameter(URL_PARAM_ID));

        mRequest.setAttribute(
                REQUEST_ATTRIB_PROFILE_USER,
                mUserDAO.getById(userId)
        );
    }
}