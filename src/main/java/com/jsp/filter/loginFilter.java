package com.jsp.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginFilter implements Filter {
    @Override
    public void init (FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getSession ().getAttribute ("user") == null && ! req.getRequestURI ().contains ("login") && ! req.getRequestURI ().contains ("reg") && ! req.getRequestURI ().contains ("forget")) {
            resp.sendRedirect (req.getContextPath () + "/index");
        }
        chain.doFilter (req, resp);
    }

    @Override
    public void destroy () {

    }
}
