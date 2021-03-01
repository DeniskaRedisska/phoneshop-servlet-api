package com.es.phoneshop.dao.impl;


import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayListOrderDaoTest {

    private ArrayListOrderDao orderDao = ArrayListOrderDao.getInstance();


    @Before
    public void setUp() {

    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();
        String secureId = UUID.randomUUID().toString();
        order.setSecureId(secureId);
        orderDao.save(order);
        assertNotNull(orderDao.getOrderBySecureId(secureId));
    }

    @Test
    public void testGetNotExistingOrder(){
        assertThrows(OrderNotFoundException.class, ()->orderDao.getOrderBySecureId("5"));
    }

}
