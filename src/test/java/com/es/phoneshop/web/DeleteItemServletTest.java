package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteItemServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CartService service;

    @InjectMocks
    private DeleteItemServlet servlet = new DeleteItemServlet();


    @Test
    public void testDeleteItem() throws ServletException, IOException {
        when(request.getSession()).thenReturn(any());
        servlet.doPost(request, response);
        when(request.getPathInfo().substring(1)).thenReturn("0");
        verify(service).delete(any(), any());
    }

    @Test
    public void testErrorDeleteItem() throws ServletException, IOException {
        when(request.getPathInfo().substring(1)).thenReturn("-1");
        servlet.doPost(request, response);
        verify(service,never()).delete(any(), any());
    }


}
