package com.es.phoneshop.service;


import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultRecentProductsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Currency;
import java.util.Deque;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentProductsServiceTest {

    @Mock
    private ArrayListProductDao productDao;

    private Currency usd = Currency.getInstance("USD");

    private Product p1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

    @InjectMocks
    private final DefaultRecentProductsService service = DefaultRecentProductsService.getInstance();

    private final Deque<Long> recentIds = new ArrayDeque<>();

    @Before
    public void setUp() {
        when(productDao.getProduct(anyLong())).thenReturn(p1);
    }

    @Test
    public void testAddUniqueId() {
        service.addToRecent(0L, recentIds);
        service.addToRecent(2L, recentIds);
        assertEquals(2, recentIds.size());
    }

    @Test
    public void testAddSameId() {
        service.addToRecent(0L,recentIds);
        service.addToRecent(0L,recentIds);
        assertEquals(1, recentIds.size());
    }

    @Test
    public void testGetRecentProducts(){
        service.addToRecent(1L,recentIds);
        service.addToRecent(2L,recentIds);
        service.addToRecent(3L,recentIds);
        service.addToRecent(4L,recentIds);
        service.addToRecent(5L,recentIds);
        service.getRecentProducts(3,recentIds);
        verify(productDao,never()).getProduct(1L);
        verify(productDao, times(3)).getProduct(anyLong());
    }

}
