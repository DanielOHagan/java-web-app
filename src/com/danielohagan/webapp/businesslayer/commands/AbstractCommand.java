package com.danielohagan.webapp.businesslayer.commands;

import com.danielohagan.webapp.error.IErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCommand {

    protected static final String REQUEST_TRUE = "true";
    protected static final String REQUEST_FALSE = "false";
    protected static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    protected static final String REQUEST_ATTRIBUTE_INFO_MESSAGE = "infoMessage";
    protected static final String REQUEST_ATTRIBUTE_HAS_ERROR = "hasError";
    protected static final String REQUEST_ATTRIBUTE_HAS_INFO = "hasInfo";

    public abstract void execute(
            HttpServletRequest request,
            HttpServletResponse response
    );

    protected void setRequestError(HttpServletRequest request, IErrorType errorType) {
        request.setAttribute(
                REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                errorType.getErrorMessage()
        );
        request.setAttribute(REQUEST_ATTRIBUTE_HAS_ERROR, REQUEST_TRUE);
    }

    protected void setRequestInfo(HttpServletRequest request, String info) {
        request.setAttribute(
                REQUEST_ATTRIBUTE_INFO_MESSAGE,
                info
        );

        request.setAttribute(
                REQUEST_ATTRIBUTE_HAS_INFO,
                REQUEST_TRUE
        );
    }
}