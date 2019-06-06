package com.danielohagan.webapp.businesslayer.commands.chat;

import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatRemoveUserCommand extends AbstractCommand {


    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {

    }
}