package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exceptions.ItemNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao, Serializable {

    private ArrayListOrderDao() {
        super(new ArrayList<>());
    }

    protected ArrayListOrderDao(List<Order> list) {
        super(list);
    }

    private static class Singleton {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return Singleton.INSTANCE;
    }


    @Override
    public Order getOrder(Long id) throws ItemNotFoundException {
        return get(id);
    }


    @Override
    public void saveOrder(Order order) {
        save(order);
    }

}
