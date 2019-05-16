package com.danielohagan.webapp.businesslayer.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCommand {

    protected static final String REQUEST_TRUE = "true";
    protected static final String REQUEST_FALSE = "false";
    protected static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";

    public abstract void execute(
            HttpServletRequest request,
            HttpServletResponse response
    );
}