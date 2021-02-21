package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ArrayListProductDao productDao;

    @InjectMocks
    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("111");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testSetAttributes() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void testDaoInvokedMethod() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(productDao).getProduct(anyLong());
    }

    @Test
    public void testGetWithIncorrectParam() {
        when(request.getPathInfo()).thenReturn("id");
        assertThrows(
                NumberFormatException.class,
                () -> servlet.doGet(request, response)
        );
    }
}


