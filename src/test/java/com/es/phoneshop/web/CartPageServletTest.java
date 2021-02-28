package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CartService service;

    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setUp(){
        when(request.getParameterValues("productId")).thenReturn(new String[]{"0","1","2"});
        when(service.getCart(any())).thenReturn(new Cart());
        when(request.getSession()).thenReturn(any());
    }

    @Test
    public void testErrorsSet() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"ee","1","2"});
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        servlet.doPost(request,response);
        verify(request).setAttribute(eq("errors"),any());
    }

    @Test
    public void testErrorsNotSet() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1","1","2"});
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        servlet.doPost(request,response);
        verify(request,never()).setAttribute(eq("errors"),any());
    }

    @Test
    public void testErrorsSetInvalidInput() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"-1","1","2"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"0","1","2"});
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        servlet.doPost(request,response);
        verify(request).setAttribute(eq("errors"),any());
    }

}
