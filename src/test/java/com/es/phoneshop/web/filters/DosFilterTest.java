package com.es.phoneshop.web.filters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    @Mock
    ServletRequest servletRequest;

    @Mock
    HttpServletResponse servletResponse;

    @Mock
    FilterChain filterChain;

    @Mock
    FilterConfig config;

    private DosFilter filter = new DosFilter();


    @Before
    public void setUp() throws ServletException {
        when(servletRequest.getRemoteAddr()).thenReturn("myAddr");
        filter.init(config);
    }

    @Test
    public void testSuccessfulDoFilter() throws IOException, ServletException {
        filter.doFilter(servletRequest,servletResponse,filterChain);
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testUnSuccessfulDoFilter() throws IOException, ServletException, InterruptedException {
        for (int i = 0; i < 30; i++) {
            filter.doFilter(servletRequest,servletResponse,filterChain);
        }
        filter.doFilter(servletRequest,servletResponse,filterChain);
        verify(servletResponse,atLeastOnce()).setStatus(429);
        Thread.sleep(60_000);
        filter.doFilter(servletRequest,servletResponse,filterChain);
        verify(filterChain,times(22)).doFilter(servletRequest, servletResponse);
    }

}
