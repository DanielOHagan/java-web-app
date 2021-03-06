package com.danielohagan.webapp.applayer.controllers.front;

import com.danielohagan.webapp.businesslayer.controllers.application.HomeApplicationController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "HomeFrontController",
        urlPatterns = /*{*/"/home"/*, "/"}*/
)
public class HomeFrontController extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        /*
        There's currently no post requests sent from the Home Page so this method is empty
         */

    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        //TODO:: Filter validation


        new HomeApplicationController(request, response).processGet();
    }
}