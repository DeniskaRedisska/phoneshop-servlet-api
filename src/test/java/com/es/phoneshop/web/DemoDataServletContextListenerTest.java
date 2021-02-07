package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        DemoDataServletContextListener spy = Mockito.spy(listener);
        spy.contextInitialized(servletContextEvent);
        verify(spy).initProducts();
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
        DemoDataServletContextListener spy = Mockito.spy(listener);
        spy.contextInitialized(servletContextEvent);
        verify(spy, never()).initProducts();
    }

}
