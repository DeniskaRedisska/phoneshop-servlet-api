package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ArrayListProductDao productDao;

    @InjectMocks
    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Captor
    ArgumentCaptor<SortType> sortTypeCaptor;

    @Captor
    ArgumentCaptor<SortField> sortFieldCaptor;

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDaoFindProducts() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("products"), any());
    }

    @Test
    public void testDaoInvokedMethod() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("query");
        when(request.getParameter("order")).thenReturn("asc");
        when(request.getParameter("sort")).thenReturn("price");
        servlet.doGet(request, response);
        verify(productDao).findProducts(anyString(), sortFieldCaptor.capture(), sortTypeCaptor.capture());
        assertEquals(SortField.price, sortFieldCaptor.getValue());
        assertEquals(SortType.asc, sortTypeCaptor.getValue());
    }
}