package com.danielohagan.webapp.applayer.controllers.front;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "SearchFrontController",
        urlPatterns = "/search/"
)
public class SearchFrontController extends HttpServlet {

    //TODO:: Search page that can search for users by username
    // with some search conditions and sorting features

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

    }
}