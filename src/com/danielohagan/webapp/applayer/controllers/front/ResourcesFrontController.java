package com.danielohagan.webapp.applayer.controllers.front;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(
//        name = "ResourcesFrontController",
//        //urlPatterns = {"/res/*"}
//)
public class ResourcesFrontController extends HttpServlet {

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

        if (request.getRequestURI().contains(".")) {
            String filePath = request.getRequestURI()
                    .replaceFirst(request.getContextPath(), "");


            System.out.println(filePath);
            request.getRequestDispatcher(filePath)
                    .forward(request, response);
        }
    }
}