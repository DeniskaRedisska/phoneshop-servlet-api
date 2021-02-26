package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrder(Long id) throws ItemNotFoundException;
    void saveOrder(Order order);
}
