package com.danielohagan.webapp.businesslayer.commands;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;
import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorCommand extends AbstractCommand {

    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        loadPage(request, response, errorResponse, JSPFileMap.ERROR_JSP);
    }
}