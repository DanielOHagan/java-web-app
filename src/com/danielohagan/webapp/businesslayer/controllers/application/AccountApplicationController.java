package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.businesslayer.commands.account.*;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.ApplicationControllerErrorType;
import com.danielohagan.webapp.error.type.ErrorType;
import com.danielohagan.webapp.error.type.IErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private ErrorResponse mAccountErrorResponse;

    public AccountApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
        mCommandMap = new HashMap<>();
        mUserDAO = new UserDAOImpl();
        mAccountErrorResponse = new ErrorResponse();

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
                command = null;
                mAccountErrorResponse.add(
                        ApplicationControllerErrorType.COMMAND_CLASS_NOT_FOUND
                );

                new ErrorCommand().execute(
                        mRequest,
                        mResponse,
                        mAccountErrorResponse
                );
                break;
        }

        if (command != null) {
            command.execute(mRequest, mResponse, mAccountErrorResponse);
        }
    }

    @Override
    public void processGet() {

        processURL();

        try {
            if (mKey == null) {
                mAccountErrorResponse.add(ErrorType.HTTP_RESPONSE_CODE_404);

                new ErrorCommand().execute(mRequest, mResponse, mAccountErrorResponse);
            } else {
                switch (mKey) {
                    case PROFILE_KEY:
                        loadProfilePage();
                        break;
                    case LOG_IN_KEY:
                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            mAccountErrorResponse.add(AccountErrorType.ALREADY_LOGGED_IN);
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    JSPFileMap.INDEX_JSP
                            );
                        } else {
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    JSPFileMap.ACCOUNT_LOG_IN_PAGE
                            );
                        }
                        break;
                    case LOG_OUT_KEY:
                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            new AccountLogOutCommand().execute(mRequest, mResponse, mAccountErrorResponse);
                        } else {
                            //TODO:: Test: I think this will send the user back to the page they were on
                            mResponse.sendRedirect(mRequest.getContextPath());
                        }
                        break;
                    case SETTINGS_KEY:
                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    JSPFileMap.ACCOUNT_SETTINGS_JSP
                            );
                        } else {
                            mAccountErrorResponse.add(AccountErrorType.NOT_LOGGED_IN);
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    JSPFileMap.INDEX_JSP
                            );
                        }
                        break;
                    case REGISTER_KEY:
                        if (SessionManager.isLoggedIn(mRequest.getSession())) {
                            mAccountErrorResponse.add(AccountErrorType.ALREADY_LOGGED_IN);
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    mRequest.getContextPath()
                            );
                        } else {
                            loadPage(
                                    mRequest,
                                    mResponse,
                                    mAccountErrorResponse,
                                    JSPFileMap.ACCOUNT_REGISTER_JSP
                            );
                        }
                        break;
                    default:
                        mAccountErrorResponse.add(ErrorType.HTTP_RESPONSE_CODE_404);
                        new ErrorCommand().execute(mRequest, mResponse, mAccountErrorResponse);
                        break;
                }
            }
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

    private void setProfilePageAttribs(int userId) {
        mRequest.setAttribute(
                REQUEST_ATTRIB_PROFILE_USER,
                mUserDAO.getById(userId)
        );
    }

    private List<IErrorType> userIdGetErrors(Integer userId) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (userId == null) {
            errorTypeList.add(AccountErrorType.NO_USER_ID);
        } else {
            if (userId < 0) {
                errorTypeList.add(AccountErrorType.ID_NOT_NEGATIVE);
            } else {
                if (!mUserDAO.exists(userId)) {
                    errorTypeList.add(AccountErrorType.DOES_NOT_EXIST);
                }
            }
        }

        return errorTypeList;
    }

    private List<IErrorType> requestParamIdGetErrors(String requestParamId) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (requestParamId == null || (requestParamId != null && requestParamId.isEmpty())) {
            errorTypeList.add(AccountErrorType.NO_USER_ID);

        } else {
            if (!requestParamId.matches("[0-9]+")) {
                errorTypeList.add(AccountErrorType.ID_NUMERIC_ONLY);
            }
        }

        //Maybe add a decimal check, however, it might not be necessary because int will truncate the value

        return errorTypeList;
    }

    private void  loadProfilePage() {
        if (!mAccountErrorResponse.contains(AccountErrorType.NO_USER_ID)) {
            String userIdString = mRequest.getParameter(URL_PARAM_ID);
            mAccountErrorResponse.add(
                    requestParamIdGetErrors(userIdString)
            );

            if (!mAccountErrorResponse.containsSeverity(ErrorSeverity.MINOR)) {
                Integer userId = Integer.parseInt(userIdString);

                ErrorResponse idIntegerErrorResponse = new ErrorResponse();
                idIntegerErrorResponse.add(
                        userIdGetErrors(userId)
                );
                mAccountErrorResponse.add(idIntegerErrorResponse.getErrorList());

                if (!idIntegerErrorResponse.containsSeverity(ErrorSeverity.MINOR)) {
                    setProfilePageAttribs(userId);
                    loadPage(
                            mRequest,
                            mResponse,
                            mAccountErrorResponse,
                            JSPFileMap.ACCOUNT_PROFILE_JSP
                    );
                } else {
                    new ErrorCommand().execute(mRequest, mResponse, mAccountErrorResponse);
                }
            } else {
                new ErrorCommand().execute(mRequest, mResponse, mAccountErrorResponse);
            }
        } else {
            new ErrorCommand().execute(mRequest, mResponse, mAccountErrorResponse);
        }
    }
}