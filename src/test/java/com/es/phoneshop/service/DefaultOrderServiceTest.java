package com.es.phoneshop.service;

import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    @Mock
    private ArrayListProductDao productDao;

    @Mock
    private ArrayListOrderDao orderDao;

    private Currency usd = Currency.getInstance("USD");

    private Product p1 = new Product(0L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

    private Product p2 = new Product(2L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");

    @InjectMocks
    private CartService cartService = DefaultCartService.getInstance();

    @InjectMocks
    private OrderService orderService = DefaultOrderService.getInstance();


    @Before
    public void setUp() {
        when(productDao.getProduct(0L)).thenReturn(p1);
        when(productDao.getProduct(2L)).thenReturn(p2);
    }

    @Test
    public void testGetOrder() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 0L, 1);
        cartService.add(cart, 2L, 1);
        Order order = orderService.getOrder(cart);
        assertEquals(order.getItems().size(), cart.getItems().size());
        assertEquals(order.getSubTotal(),cart.getTotalCost());
        assertNotNull(order.getDeliveryCost());
        assertNotNull(order.getTotalCost());
    }

    @Test
    public void testPlaceOrder() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 0L, 1);
        cartService.add(cart, 2L, 1);
        Order order = orderService.getOrder(cart);
        orderService.placeOrder(order);
        assertNotNull(order.getSecureId());
        verify(orderDao).saveOrder(order);
    }

}
