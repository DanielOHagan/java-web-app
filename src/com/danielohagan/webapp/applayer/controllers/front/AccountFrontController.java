package com.danielohagan.webapp.applayer.controllers.front;

import com.danielohagan.webapp.businesslayer.controllers.application.AccountApplicationController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "AccountFrontController",
        urlPatterns = "/account/*"
)
public class AccountFrontController extends HttpServlet {
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {


        new AccountApplicationController(request, response).processPost();
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        

        new AccountApplicationController(request, response).processGet();
    }
}