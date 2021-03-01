package com.es.phoneshop.web.filters;


import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.impl.DefaultDosProtectionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {

    private DosProtectionService dosProtectionService;

    private final int TOO_MANY_REQUESTS = 429;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(servletRequest.getRemoteAddr())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(TOO_MANY_REQUESTS);
        }
    }

    @Override
    public void destroy() {

    }
}
