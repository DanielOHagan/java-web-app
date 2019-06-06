package com.danielohagan.webapp.businesslayer.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(
        filterName = "AccountAccessFilter",
        servletNames = {
                "AccountFrontController",
                "ChatFrontController"
        }
)
public class AccountAccessFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(
            ServletRequest req,
            ServletResponse resp,
            FilterChain chain
    ) throws ServletException, IOException {

        /*
        If On page that needs user to be logged in or have certain
        permissions, then redirect user session if they do not have
        correct permissions
         */

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}