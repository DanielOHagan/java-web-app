package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.chat.ChatManager;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.ChatErrorType;
import com.danielohagan.webapp.error.type.IErrorType;
import com.danielohagan.webapp.error.type.SessionErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ChatApplicationController extends AbstractApplicationController {

    private final String CHAT_SESSION_PARAM = "chatSessionId";

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private ChatSessionDAOImpl mChatSessionDAO;
    private MessageDAOImpl mMessageDAO;
    private ChatManager mChatManager;
    private ErrorResponse mChatCentreErrorResponse;

    public ChatApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
        mChatManager = new ChatManager();
        mChatCentreErrorResponse = new ErrorResponse();
    }

    @Override
    public void processGet() {
        //Store the chat sessions into the User instance stored in the HttpSession,

        if (!SessionManager.isLoggedIn(mRequest.getSession())) {
            mChatCentreErrorResponse.add(AccountErrorType.NOT_LOGGED_IN);
            new ErrorCommand().execute(
                    mRequest,
                    mResponse,
                    mChatCentreErrorResponse
            );
        } else {
            processURL();

            Integer primaryChatSessionId =
                    mChatManager.getPrimaryChatSessionId();
            User user = SessionManager.getCurrentUser(mRequest.getSession());

            if (user != null) {
                if (!mChatCentreErrorResponse.containsSeverity(ErrorSeverity.FATAL)) {
                    loadChatCentrePage(user, primaryChatSessionId);
                } else {
                    new ErrorCommand().execute(
                            mRequest,
                            mResponse,
                            mChatCentreErrorResponse
                    );
                }
            } else {
                mChatCentreErrorResponse.add(SessionErrorType.FAILED_TO_RETRIEVE_CURRENT_USER);
                new ErrorCommand().execute(
                        mRequest,
                        mResponse,
                        mChatCentreErrorResponse
                );
            }
        }
    }


    @Override
    public void processPost() {
        /*
        Initial testing to get the database sorted will use POST requests
        Final iteration will use Websocket so POST requests will not be needed
         (at least for the general chat functions such as sending and retrieving messages)
         */
        if (!SessionManager.isLoggedIn(mRequest.getSession())) {
            mChatCentreErrorResponse.add(AccountErrorType.NOT_LOGGED_IN);
            new ErrorCommand().execute(
                    mRequest,
                    mResponse,
                    mChatCentreErrorResponse
            );
        } else {

        }
    }

    @Override
    protected void processURL() {
        String chatSessionIdString = mRequest.getParameter(CHAT_SESSION_PARAM);
        Integer chatSessionId = null;

        //Extract errors from the ID URI Parameter and store them in mChatCentreErrorResponse
        List<IErrorType> idParamErrorList = requestParamIdGetErrors(chatSessionIdString);
        if (idParamErrorList != null && !idParamErrorList.isEmpty()) {
            mChatCentreErrorResponse.add(idParamErrorList);
        }

        if (!mChatCentreErrorResponse.contains(ChatErrorType.NO_SESSION_ID)) {
            chatSessionId = Integer.parseInt(
                    mRequest.getParameter(CHAT_SESSION_PARAM)
            );

            //Extract errors from Session ID and store them in a temp ErrorResponse
            List<IErrorType> sessionIdErrorList =
                    chatSessionIdGetErrors(chatSessionId);
            ErrorResponse sessionIdErm = new ErrorResponse();
            if (sessionIdErrorList != null && !sessionIdErrorList.isEmpty()) {
                sessionIdErm.add(sessionIdErrorList);
                mChatCentreErrorResponse.add(sessionIdErrorList);
            }

            //Checks for at most Minor because anything higher means the requested Chat Session does not work
            if (!sessionIdErm.containsSeverity(ErrorSeverity.MINOR)) {
                mChatManager.setPrimaryChatSessionId(chatSessionId);
            }
        }
    }

    private List<IErrorType> chatSessionIdGetErrors(Integer chatSessionId) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (chatSessionId == null) {
            errorTypeList.add(ChatErrorType.NO_SESSION_ID);
        } else {
            if (chatSessionId < 0) {
                errorTypeList.add(ChatErrorType.SESSION_ID_NOT_NEGATIVE);
            } else {
                if (!mChatSessionDAO.exists(chatSessionId)) {
                    errorTypeList.add(ChatErrorType.SESSION_DOES_NOT_EXIST);
                }
            }
        }

        return errorTypeList;
    }

    private List<IErrorType> requestParamIdGetErrors(String requestParamId) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (requestParamId == null || (requestParamId != null && requestParamId.isEmpty())) {
            errorTypeList.add(ChatErrorType.NO_SESSION_ID);
        } else {
            if (!requestParamId.matches("[0-9]+")) {
                errorTypeList.add(ChatErrorType.SESSION_ID_NUMERIC_ONLY);
            }
        }

        //Maybe add a decimal check, however, it might not be necessary because int will truncate the value

        return errorTypeList;
    }

    private void loadChatCentrePage(User user, Integer primaryChatSessionId) {
        List<ChatSession> chatSessionList =
                mChatSessionDAO.getChatSessionList(
                        user.getId()
                );
        SessionManager.setChatSessionList(
                mRequest.getSession(),
                chatSessionList
        );

        if (primaryChatSessionId != null) {
            ChatSession primaryChatSession =
                    mChatSessionDAO.getById(primaryChatSessionId);

            //Get the message list if the user has access to the requested Chat Session
            if(mChatSessionDAO.userIsInChatSession(
                    user.getId(),
                    primaryChatSessionId
            )) {
                primaryChatSession.setMessageList(
                        mMessageDAO.getChatSessionMessageList(primaryChatSessionId)
                );
                primaryChatSession.setUserList(
                        mChatSessionDAO.getUserListBySession(primaryChatSessionId)
                );

                SessionManager.setPrimaryChatSession(
                        mRequest.getSession(),
                        primaryChatSession
                );
            } else  {
                SessionManager.setPrimaryChatSessionToDefault(
                        mRequest.getSession()
                );
                mChatCentreErrorResponse.add(
                        ChatErrorType.ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION
                );
            }
        }

        loadPage(
                mRequest,
                mResponse,
                mChatCentreErrorResponse,
                JSPFileMap.CHAT_CENTRE_JSP
        );
    }
}