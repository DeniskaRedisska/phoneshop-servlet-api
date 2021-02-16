package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {

    @Mock
    private ServletContextEvent servletContextEvent;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Product product;

    @Mock
    private ArrayListProductDao productDao;

    @Spy
    @InjectMocks
    private final DemoDataServletContextListener listener = new DemoDataServletContextListener();

    @Before
    public void setup() {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(productDao.getProduct(any())).thenReturn(product);
    }

    @Test
    public void testContextInitializedWithDemoData() {
        when((servletContext.getInitParameter(anyString()))).thenReturn("true");
        listener.contextInitialized(servletContextEvent);
        verify(listener).initProducts();
    }

    @Test
    public void testInsertDemoData() {
        when((servletContext.getInitParameter(anyString()))).thenReturn("true");
        listener.contextInitialized(servletContextEvent);
        verify(productDao, atLeastOnce()).save(any());
    }

    @Test
    public void testContextInitializedWithoutDemoData() {
        when((servletContext.getInitParameter(anyString()))).thenReturn("false");
        listener.contextInitialized(servletContextEvent);
        verify(listener, never()).initProducts();
    }

}
