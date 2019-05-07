package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.applayer.utils.JSPFileMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeApplicationController extends AbstractApplicationController {

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;

    public HomeApplicationController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        mRequest = request;
        mResponse = response;
    }

    @Override
    public void processGet() {
        //TODO:: Session attributes

        //TODO:: Request attributes

        //Forward dispatcher
        try {
            mRequest.getRequestDispatcher(JSPFileMap.HOME_PAGE).forward(mRequest, mResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processPost() {
        /*
        There's currently no post requests sent
        from the Home Page so this method is empty
         */
    }

    @Override
    protected void processURL() {

    }
}