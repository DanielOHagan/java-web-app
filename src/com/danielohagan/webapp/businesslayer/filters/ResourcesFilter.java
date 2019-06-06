package com.danielohagan.webapp.businesslayer.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(
        filterName = "ResourcesFilter",
        urlPatterns = "/res/*"
)
public class ResourcesFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(
            ServletRequest req,
            ServletResponse resp,
            FilterChain chain
    ) throws ServletException, IOException {



        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }
}