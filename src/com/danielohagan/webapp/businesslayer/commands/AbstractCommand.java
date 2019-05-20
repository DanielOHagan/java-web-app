package com.danielohagan.webapp.businesslayer.commands;

import com.danielohagan.webapp.error.type.IErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCommand {

    private static final String REQUEST_TRUE = "true";
    private static final String REQUEST_FALSE = "false";

    private static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    private static final String REQUEST_ATTRIBUTE_INFO_MESSAGE = "infoMessage";

    private static final String REQUEST_ATTRIBUTE_HAS_ERROR = "hasError";
    private static final String REQUEST_ATTRIBUTE_HAS_INFO = "hasInfo";

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