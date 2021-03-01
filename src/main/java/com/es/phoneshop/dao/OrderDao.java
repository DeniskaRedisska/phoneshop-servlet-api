package com.es.phoneshop.dao;

import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao extends GenericDao<Order> {
    Order getOrderBySecureId(String id) throws OrderNotFoundException;
}
