package com.es.phoneshop.service;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.InvalidArgumentException;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.exceptions.VerificationException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private DataProvider<Cart> dataProvider;

    @Mock
    private ArrayListProductDao productDao;

    private Currency usd = Currency.getInstance("USD");

    private Product p1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

    private Product p2 = new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");

    private Cart cart = new Cart();

    @InjectMocks
    private final DefaultCartService cartService = DefaultCartService.getInstance();

    @Before
    public void setUp() {
        when(productDao.getProduct(0L)).thenReturn(p1);
        when(productDao.getProduct(2L)).thenReturn(p2);

    }

    @Test
    public void testGetCart() {
        cartService.getCart(dataProvider);
        verify(dataProvider).getAttribute(anyString(), any());
    }

    @Test
    public void testAddToCartSameProduct() throws InvalidArgumentException, OutOfStockException {
        cart.getItems().add(new CartItem(p1, 5));
        cartService.add(cart, 0L, 10);
        assertEquals(1, cart.getItems().size());
        assertEquals(15, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddToCartDifferentProduct() throws InvalidArgumentException, OutOfStockException {
        cart.getItems().add(new CartItem(p1, 5));
        cartService.add(cart, 2L, 5);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    public void testOutOfStock() throws InvalidArgumentException, OutOfStockException {
        cartService.add(cart, 0L, 50);
        assertThrows(OutOfStockException.class, () -> cartService.add(cart, 0L, 60));
    }

    @Test
    public void testInvalidQuantity() {
        assertThrows(InvalidArgumentException.class, () -> cartService.add(cart, 0L, -50));
    }

    @Test
    public void testNullIdOrNullCart() {
        assertThrows(VerificationException.class, () -> cartService.add(null, 0L, 1));
        assertThrows(VerificationException.class, () -> cartService.add(cart, null, 1));
    }

}
