package com.danielohagan.webapp.applayer.controllers.front;

import com.danielohagan.webapp.businesslayer.controllers.application.ChatApplicationController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "ChatFrontController",
        urlPatterns = "/chat/"
)
public class ChatFrontController extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        new ChatApplicationController(request, response).processGet();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        new ChatApplicationController(request, response).processPost();
    }
}