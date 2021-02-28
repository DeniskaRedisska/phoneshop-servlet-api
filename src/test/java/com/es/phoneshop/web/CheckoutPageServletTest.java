package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CartService testCartService;

    @Mock
    private RequestDispatcher dispatcher;

    private Currency usd = Currency.getInstance("USD");

    private Product p1 = new Product(0L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

    private Product p2 = new Product(2L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");

    private OrderService orderService = DefaultOrderService.getInstance();

    @Mock
    private ArrayListProductDao productDao;

    @InjectMocks
    CartService cartService = DefaultCartService.getInstance();


    @InjectMocks
    private final CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setUp() throws OutOfStockException {
        when(productDao.getProduct(0L)).thenReturn(p1);
        when(productDao.getProduct(2L)).thenReturn(p2);
        Cart cart = new Cart();
        cartService.add(cart, 0L, 1);
        cartService.add(cart, 2L, 1);
        when(testCartService.getCart(any())).thenReturn(cart);
        when(request.getSession()).thenReturn(session);
        servlet.setOrderService(orderService);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }


    @Test
    public void testSuccessDoPost() throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn("denis");
        when(request.getParameter("lastName")).thenReturn("denis");
        when(request.getParameter("phoneNumber")).thenReturn("+375297611500");
        when(request.getParameter("deliveryAddress")).thenReturn("Esenina 56");
        when(request.getParameter("deliveryDate")).thenReturn("2020-08-09");
        when(request.getParameter("paymentMethod")).thenReturn("CASH");
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWithInvalidPhone() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("denis");
        when(request.getParameter("lastName")).thenReturn("denis");
        when(request.getParameter("phoneNumber")).thenReturn("+375297611500000000");
        when(request.getParameter("deliveryAddress")).thenReturn("Esenina 56");
        when(request.getParameter("deliveryDate")).thenReturn("2020-08-09");
        when(request.getParameter("paymentMethod")).thenReturn("CASH");
        servlet.doPost(request, response);
        verify(request,atLeastOnce()).setAttribute(anyString(),any());
    }

    @Test
    public void testDoPostWithInvalidDate() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("denis");
        when(request.getParameter("lastName")).thenReturn("denis");
        when(request.getParameter("phoneNumber")).thenReturn("+375297611500");
        when(request.getParameter("deliveryAddress")).thenReturn("Esenina 56");
        when(request.getParameter("deliveryDate")).thenReturn("20200809");
        when(request.getParameter("paymentMethod")).thenReturn("CASH");
        servlet.doPost(request, response);
        verify(request,atLeastOnce()).setAttribute(anyString(),any());
    }

    @Test
    public void testDoPostWithInvalidPaymentMethod() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("denis");
        when(request.getParameter("lastName")).thenReturn("denis");
        when(request.getParameter("phoneNumber")).thenReturn("+375297611500");
        when(request.getParameter("deliveryAddress")).thenReturn("Esenina 56");
        when(request.getParameter("deliveryDate")).thenReturn("2020-08-09");
        when(request.getParameter("paymentMethod")).thenReturn("");
        servlet.doPost(request, response);
        verify(request,atLeastOnce()).setAttribute(anyString(),any());
    }
}
