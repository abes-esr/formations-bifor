/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class IE9CompatablityFixServlet implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).setHeader("X-UA-Compatible", "IE=EmulateIE7"); // or IE=EmulateIE8 chain.doFilter(request, response); 
    }

    public void destroy() {
    }
}
