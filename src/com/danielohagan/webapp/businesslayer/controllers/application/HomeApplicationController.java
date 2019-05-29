package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.businesslayer.commands.ErrorCommand;
import com.danielohagan.webapp.error.response.ErrorResponse;
import com.danielohagan.webapp.error.type.ErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeApplicationController extends AbstractApplicationController {

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private String mKey;
    private ErrorResponse mHomeErrorResponse;

    public HomeApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
        mHomeErrorResponse = new ErrorResponse();
    }

    @Override
    public void processGet() {

        processURL();

        if (mKey != null && !mKey.equals("home")) {
            mHomeErrorResponse.add(ErrorType.HTTP_RESPONSE_CODE_404);
            new ErrorCommand().execute(mRequest, mResponse, mHomeErrorResponse);
        } else {
            loadPage(mRequest, mResponse, mHomeErrorResponse, JSPFileMap.INDEX_JSP);
        }
    }

    @Override
    public void processPost() {
        /*
        There's currently no post requests sent
        from the Home Page so this method is empty
         */
    }

    @Override
    protected void processURL() {
        String uri = mRequest.getRequestURI();
        mKey = null;

        if (uri.contains("/")) {
            uri = uri.replaceFirst("/", "");

            String[] uriKeys = uri.split("/");

            if (uriKeys.length > 1) {
                mKey = uriKeys[uriKeys.length - 1].toLowerCase();
            }
        }
    }
}