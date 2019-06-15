package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.session.SessionManager;
import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;
import com.danielohagan.webapp.datalayer.dao.implementations.ChatSessionDAOImpl;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.AccountErrorType;
import com.danielohagan.webapp.error.type.SessionErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ChatApplicationController extends AbstractApplicationController {

    private final String CHAT_SESSION_PARAM = "chatSessionId";

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private ChatSessionDAOImpl mChatSessionDAO;
    private MessageDAOImpl mMessageDAO;
    private ErrorResponse mChatCentreErrorResponse;

    public ChatApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
        mChatSessionDAO = new ChatSessionDAOImpl();
        mMessageDAO = new MessageDAOImpl();
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
            User user = SessionManager.getCurrentUser(mRequest.getSession());

            if (user != null) {
                if (!mChatCentreErrorResponse.containsSeverity(ErrorSeverity.FATAL)) {
                    loadChatCentrePage(user);
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

    }

    private void loadChatCentrePage(User user) {
        List<ChatSession> chatSessionList =
                mChatSessionDAO.getChatSessionList(
                        user.getId()
                );
        mRequest.setAttribute(
                "chatSessionList",
                chatSessionList
        );

        loadPage(
                mRequest,
                mResponse,
                mChatCentreErrorResponse,
                JSPFileMap.CHAT_CENTRE_JSP
        );
    }
}