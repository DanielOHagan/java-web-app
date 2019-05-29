package com.danielohagan.webapp.businesslayer;

import com.danielohagan.webapp.error.response.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractRequestManager {

    /*
    Provides basic methods to help with controlling how the HttpServletRequest
    is handled in the application
     */

    private static final String REQUEST_TRUE = "true";
    private static final String REQUEST_FALSE = "false";

    private static final String REQUEST_ATTRIBUTE_ERROR_LIST = "errorList";
    private static final String REQUEST_ATTRIBUTE_INFO_LIST = "infoList";

    private static final String REQUEST_ATTRIBUTE_HAS_ERROR = "hasError";
    private static final String REQUEST_ATTRIBUTE_HAS_INFO = "hasInfo";

    protected void setRequestErrorResponse(
            HttpServletRequest request,
            ErrorResponse errorResponse
    ) {
        if (errorResponse.hasInfo()) {

            request.setAttribute(
                    REQUEST_ATTRIBUTE_INFO_LIST,
                    errorResponse.getOnlyInfo()
            );

            request.setAttribute(
                    REQUEST_ATTRIBUTE_HAS_INFO,
                    REQUEST_TRUE
            );
        }

        if (errorResponse.hasError()){

            request.setAttribute(
                    REQUEST_ATTRIBUTE_ERROR_LIST,
                    errorResponse.getOnlyErrors()
            );

            request.setAttribute(
                    REQUEST_ATTRIBUTE_HAS_ERROR,
                    REQUEST_TRUE
            );
        }
    }

    protected void loadPage(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorResponse errorResponse,
            String destination
    ) {

        setRequestErrorResponse(request, errorResponse);

        try {
            request.getRequestDispatcher(destination)
                    .forward(request, response);
        } catch (ServletException e) {
            redirectToHome(request, response);

            e.printStackTrace();
        } catch (IOException e) {
            redirectToHome(request, response);

            e.printStackTrace();
        }
    }

    protected void redirectToHome(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            response.sendRedirect(request.getContextPath() + "/");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}