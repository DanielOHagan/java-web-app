package com.danielohagan.webapp.businesslayer.commands;

import com.danielohagan.webapp.businesslayer.AbstractRequestManager;
import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCommand extends AbstractRequestManager {

    public abstract void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    );
}