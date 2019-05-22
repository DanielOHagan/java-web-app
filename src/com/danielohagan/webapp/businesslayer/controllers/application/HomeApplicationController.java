package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.error.type.ErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeApplicationController extends AbstractApplicationController {

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private String mKey;

    public HomeApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
    }

    @Override
    public void processGet() {

        processURL();

        if (mKey != null && !mKey.equals("home")) {
            mRequest.setAttribute(
                    REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                    ErrorType.HTTP_RESPONSE_CODE_404
            );

            try {
                mRequest.getRequestDispatcher(JSPFileMap.ERROR_JSP)
                        .forward(mRequest, mResponse);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mRequest.getRequestDispatcher(JSPFileMap.INDEX_JSP)
                        .forward(mRequest, mResponse);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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