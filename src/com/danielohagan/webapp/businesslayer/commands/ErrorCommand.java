package com.danielohagan.webapp.businesslayer.commands;

import com.danielohagan.webapp.error.ErrorType;
import com.danielohagan.webapp.error.IErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorCommand extends AbstractCommand {

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        execute(request, response, ErrorType.UNKNOWN_ERROR);
    }

    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            IErrorType errorType
    ) {
        //TODO:: Forward the user to the error page with the error type

    }


}