package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    Order getOrderBySecureId(String id) throws OrderNotFoundException;

    void saveOrder(Order order);
}
