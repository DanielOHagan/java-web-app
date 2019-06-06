package com.danielohagan.webapp.businesslayer.commands.chat;

import com.danielohagan.webapp.businesslayer.commands.AbstractCommand;
import com.danielohagan.webapp.datalayer.dao.implementations.MessageDAOImpl;
import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatSendMessageCommand extends AbstractCommand {

    private final String HTML_FORM_MESSAGE_BOX = "chatDialogueBoxMessageInput";

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse
    ) {
        MessageDAOImpl messageDAO = new MessageDAOImpl();
        String messageBody = request.getParameter(HTML_FORM_MESSAGE_BOX);


    }
}